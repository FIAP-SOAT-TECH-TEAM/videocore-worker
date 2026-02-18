FROM ghcr.io/graalvm/native-image-community:latest AS build
WORKDIR /app

# Copiar arquivos de dependências e build
COPY worker/gradle/ ./gradle/
COPY worker/gradlew worker/build.gradle worker/settings.gradle ./ 
RUN chmod +x ./gradlew

# Criar arquivo custom.security para desabilitar verificação de assinatura
RUN mkdir -p custom_security
RUN printf "jdk.jar.disabledAlgorithms=MD2, MD5, RSA, DSA\n" > custom_security/custom.security

# Download de dependências
RUN microdnf install -y findutils

# Copiar código fonte
COPY worker/src/ ./src/

# Copiar configuração do Spotless
COPY worker/spotless-formatter.xml ./

# Construir o aplicativo AOT (native image)
# Considerações sobre o Build Time: https://www.graalvm.org/latest/reference-manual/native-image/overview/BuildOutput/#graal-compiler (-O2 Default - Performance Produção)
RUN ./gradlew nativeCompile \
  --no-fallback \
  # https://github.com/bytedeco/gradle-javacpp?tab=readme-ov-file#the-platform-plugin
  -PjavacppPlatform=linux-x86_64 \
  # https://github.com/Azure/azure-sdk-for-java/blob/main/sdk/spring/README.md#spring-boot-3-support
  --build-args="-J-Djava.security.properties=/app/custom_security/custom.security" \
  # https://github.com/Azure/azure-sdk-for-java/issues/34775
  --build-args="--initialize-at-build-time=org.slf4j.helpers,org.slf4j.LoggerFactory,ch.qos.logback,com.fasterxml.jackson,net.logstash.logback,org.yaml.snakeyaml,tools.jackson.core.util" \
  --no-daemon

# Imagem final
# https://github.com/bytedeco/javacv/issues/2212
FROM debian:12-slim AS runtime
WORKDIR /app

# Adicionar ferramentas básicas
RUN apt-get update && \
    apt-get install -y \
        curl \
        wget \
        tzdata \
    && rm -rf /var/lib/apt/lists/*

# Definir timezone para GMT-3 (America/Sao_Paulo)
RUN ln -sf /usr/share/zoneinfo/America/Sao_Paulo /etc/localtime && \
    echo "America/Sao_Paulo" > /etc/timezone

# Adicionar usuário não-root para segurança
RUN addgroup --system appgroup && \
    adduser --system --ingroup appgroup appuser

# Copiar binários nativos gerados pelo GraalVM
COPY --from=build /app/build/native/nativeCompile/lib*.so /usr/lib/
COPY --chmod=0755 --from=build /app/build/native/nativeCompile/* worker

# Mudar para usuário não-root
USER appuser

# Porta da aplicação (modificável via docker-compose)
EXPOSE 9090

# Verificação de saúde interna
HEALTHCHECK --interval=30s --timeout=10s --retries=3 \
  CMD wget --spider -q http://localhost:9090/actuator/health || exit 1

# Iniciar aplicação nativa
ENTRYPOINT ["/app/worker"]