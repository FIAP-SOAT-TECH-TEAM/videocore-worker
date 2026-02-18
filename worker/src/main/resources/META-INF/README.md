# Gerar binário nativo com GraalVM JDK 25 usando Tracing Agent (Linux x86_64)

Este guia descreve como:

- Instalar o GraalVM JDK 25 no Linux  
- Coletar metadata automaticamente com o Tracing Agent  
- Preparar a aplicação para compilação com Native Image  

O objetivo é gerar corretamente os arquivos de configuração exigidos pelo modelo de compilação do Native Image.

> ⚠️ IMPORTANTE  
O `bootJar` e a execução com o `native-image-agent` **devem ser feitos em uma distribuição Linux x86_64**, pois:
>
> - O agente captura referências JNI reais
>- As bibliotecas nativas serão `.so`
>- O `reachability-metadata.json` refletirá corretamente o ambiente >Linux
>- A aplicação será executada posteriormente em container Linux
>
>Gerar metadata em Windows pode resultar em referências incorretas >(DLL) e falhas em runtime no container.

---

# Conceito fundamental: Closed World

O Native Image opera sob o princípio de **Closed World Assumption**.

Isso significa que, no momento da compilação, o compilador precisa conhecer **todo o grafo de classes, métodos e recursos que poderão ser utilizados em runtime**.

Diferença em relação à JVM tradicional:

- A JVM resolve classes dinamicamente em tempo de execução.
- O Native Image resolve tudo antecipadamente durante o build.

Consequências:

Recursos dinâmicos não são detectados automaticamente:

- Reflection  
- JNI  
- Proxies dinâmicos  
- Carregamento de recursos  
- Bibliotecas que acessam classes indiretamente  

Se não forem registrados, podem ocorrer erros como:

- `ClassNotFoundException`
- `NoSuchMethodException`
- `UnsatisfiedLinkError`

O **Tracing Agent** resolve isso registrando automaticamente o comportamento dinâmico da aplicação durante a execução real.

---

# 1. Instalar GraalVM JDK 25 (Linux x86_64)

## Motivo

O Native Image não funciona com um JDK convencional. É necessário o GraalVM, que fornece:

- Compilador AOT (Ahead-of-Time)
- Ferramenta `native-image`
- Tracing Agent

## Procedimento

Baixe o GraalVM JDK 25 para Linux (x86_64) e extraia para:

```
/java/graalvmjdk/graalvm-jdk-25.0.2+10.1
```

Crie o arquivo de configuração:

```
sudo vi /etc/profile.d/javajdk.sh
```

Conteúdo:

```
export JAVA_HOME=/java/graalvmjdk/graalvm-jdk-25.0.2+10.1
export PATH=$JAVA_HOME/bin:$PATH
```

Aplique:

```
source /etc/profile.d/javajdk.sh
```

Valide:

```
java -version
```

---

# 2. Criar diretório para metadata

O Native Image procura arquivos de configuração em:

```
META-INF/native-image/
```

Na raiz do projeto:

```
mkdir -p worker/src/main/resources/META-INF/native-image
```

Estrutura esperada:

```
worker/
└── src/
    └── main/
        └── resources/
            └── META-INF/
                └── native-image/
```

---

# 3. Gerar o JAR executável (Linux x86_64)

O Tracing Agent precisa executar a aplicação real para observar comportamentos dinâmicos.

Execute:

```
./gradlew bootJar
```

Será gerado:

```
build/libs/worker-0.0.1.jar
```

> ⚠️ Este passo deve ocorrer em Linux x86_64 para garantir coerência com o ambiente final do container.

---

# 4. Executar com Tracing Agent (Linux x86_64)

Entre na pasta:

```
cd build/libs
```

Execute:

```
java -agentlib:native-image-agent=config-merge-dir=../../src/main/resources/META-INF/native-image/ -jar worker-0.0.1.jar
```
> ⚠️ O arquivo de variáveis de ambiente utilizado em tempo de desenvolvimento com o profile `local`, presente na raiz do projeto, deve ser copiado para cá antes da execução. Logo, o ambiente de desenvolvimento (fornecido pelos scripts bash) precisa estar funcionando.

Parâmetro utilizado:

```
-agentlib:native-image-agent=config-merge-dir=<diretório>
```

Esse parâmetro:

- Ativa o agente
- Gera ou mescla arquivos de configuração
- Produz `reachability-metadata.json`
- Registra uso real de JNI e bibliotecas `.so`

> ⚠️ Este passo deve ocorrer em Linux x86_64 para garantir coerência com o ambiente final do container.

---

# 5. Exercitar todos os fluxos

O agente registra apenas o que for executado.

Se determinado fluxo não for acionado:

- A metadata não será gerada
- O binário poderá falhar em produção

Durante a execução:

- Acesse todas as rotas
- Processe mensagens
- Simule eventos
- Execute fluxos que utilizem JNI
- Acione integrações com bibliotecas nativas

Após finalizar os testes, encerre a aplicação.

Os arquivos estarão em:

```
worker/src/main/resources/META-INF/native-image
```

---

# Resultado esperado

Arquivo gerado automaticamente:

```
reachability-metadata.json
```

Esse arquivo será consumido pelo `native-image` durante a compilação do binário final.

---

# Resumo técnico

| Etapa | Finalidade |
|-------|------------|
| Instalar GraalVM Linux | Disponibilizar AOT, native-image e agent |
| Configurar JAVA_HOME | Garantir uso do GraalVM correto |
| Criar META-INF/native-image | Diretório padrão de metadata |
| Executar bootJar em Linux | Produzir artefato compatível com container |
| Executar com agent em Linux | Capturar JNI e referências `.so` corretas |
| Exercitar aplicação | Garantir cobertura completa |

---

# Referências

Documentação Native Image:
https://www.graalvm.org/latest/reference-manual/native-image/

Guia oficial Tracing Agent:
https://www.graalvm.org/latest/reference-manual/native-image/metadata/AutomaticMetadataCollection/

Repositório oficial de reachability metadata:
https://github.com/oracle/graalvm-reachability-metadata