# ⚙️ VideoCore Worker

<div align="center">

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=FIAP-SOAT-TECH-TEAM_videocore-worker&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=FIAP-SOAT-TECH-TEAM_videocore-worker)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=FIAP-SOAT-TECH-TEAM_videocore-worker&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=FIAP-SOAT-TECH-TEAM_videocore-worker)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=FIAP-SOAT-TECH-TEAM_videocore-worker&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=FIAP-SOAT-TECH-TEAM_videocore-worker)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=FIAP-SOAT-TECH-TEAM_videocore-worker&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=FIAP-SOAT-TECH-TEAM_videocore-worker)

</div>

Microsserviço de processamento de vídeo do ecossistema VideoCore, responsável por extrair frames de vídeos utilizando FFmpeg e gerar screenshots. Desenvolvido como parte do curso de Arquitetura de Software da FIAP (Tech Challenge).

<div align="center">
  <a href="#visao-geral">Visão Geral</a> •
  <a href="#arquitetura">Arquitetura</a> •
  <a href="#repositorios">Repositórios</a> •
  <a href="#tecnologias">Tecnologias</a> •
  <a href="#instalacao">Instalação</a> •
  <a href="#deploy">Fluxo de Deploy</a> •
  <a href="#contribuicao">Contribuição</a>
</div><br>

> 📽️ Vídeo de demonstração da arquitetura: [https://youtu.be/k3XbPRxmjCw](https://youtu.be/k3XbPRxmjCw)<br>

---

<h2 id="visao-geral">📋 Visão Geral</h2>

O **VideoCore Worker** é o microsserviço responsável pelo processamento de vídeos do sistema. Ele escuta eventos de criação de blobs no Azure Storage via Service Bus, realiza a extração de frames utilizando **FFmpeg (JavaCV)** e publica os screenshots processados de volta no Blob Storage.

### Principais Responsabilidades

- **Processamento de Vídeo**: Extração de frames em intervalos configuráveis via FFmpeg
- **Download**: Busca vídeos do Azure Blob Storage para processamento
- **Upload**: Envia screenshots e arquivos ZIP para o Blob Storage
- **Eventos**: Consome e publica eventos de status via Azure Service Bus
- **Gerenciamento de Arquivos**: Controle de arquivos temporários durante processamento

### Fluxo de Processamento

```
1. BlobCreated Event (Service Bus)
        ↓
2. Download do vídeo (Azure Blob Storage)
        ↓
3. Extração de frames (FFmpeg/JavaCV)
        ↓
4. Upload de screenshots (Azure Blob Storage)
        ↓
5. Publicação de status (Service Bus → Reports)
        ↓
6. Limpeza de arquivos temporários
```

### Status de Processamento

| Status | Descrição |
|--------|-----------|
| `STARTED` | Upload recebido, processamento iniciado |
| `PROCESSING` | Extração de frames em andamento |
| `COMPLETED` | Screenshots prontos para download |
| `FAILED` | Erro no processamento |

---

<h2 id="arquitetura">🧱 Arquitetura</h2>

<details>
<summary>Expandir para mais detalhes</summary>

### 🎯 Clean Architecture

O projeto segue os princípios de **Clean Architecture** com separação clara de responsabilidades:

```
core/
├── domain/           # Entidades de vídeo e regras de negócio
├── usecases/         # Casos de uso de processamento
└── interfaceadapters/
    └── mapper/       # Conversão de eventos ↔ domínio

infrastructure/
├── in/               # Adaptadores de entrada
│   └── event/azsvcbus/
│       ├── listener/     # ProcessListener (Service Bus)
│       ├── handler/      # ProcessHandler (message handling)
│       └── config/       # ProcessConfig (Spring config)
├── out/              # Adaptadores de saída
│   ├── process/          # FfmpegProcessor (video processing)
│   ├── persistence/
│   │   ├── file/         # DefaultFileSource (temp files)
│   │   └── blobstorage/  # AzureBlobStorageRepository
│   └── event/azsvcbus/   # AzSvcEventSender (status publishing)
└── common/           # Configurações, sources, exceções
```

### 🎬 Processamento de Vídeo

```
ProcessListener (Service Bus Event)
    ↓
ProcessHandler (Message Parsing)
    ↓
Use Case (Business Logic)
    ↓
AzureBlobStorageRepository.download(video)
    ↓
FfmpegProcessor.extractFrames(video, interval)
    ↓
AzureBlobStorageRepository.upload(images + zip)
    ↓
AzSvcEventSender.publish(status: COMPLETED)
```

### 🔌 Integrações

| Serviço | Tipo | Descrição |
|---------|------|-----------|
| **Azure Service Bus** | Assíncrona | Consumo de `BlobCreated` events + publicação de status |
| **Azure Blob Storage** | Síncrona | Download de vídeos, upload de screenshots e ZIPs |
| **FFmpeg (JavaCV)** | Local | Extração de frames de vídeo |

### 📊 Observabilidade

- **Traces**: OpenTelemetry (OTLP gRPC)
- **Métricas**: Micrometer (OTLP gRPC)
- **Logs**: Logstash JSON format
- **Health Checks**: Spring Actuator (`/actuator/health`)

### ☸️ Kubernetes

- **Auto-scaling**: KEDA (ScaledObject) baseado em mensagens do Service Bus
- **TriggerAuthentication**: Autenticação KEDA com Service Bus
- **Azure Key Vault Provider**: Secrets via CSI driver

### 📦 Estrutura do Projeto

```
videocore-worker/
├── worker/
│   ├── build.gradle              # Dependências (JavaCV, FFmpeg)
│   ├── src/main/
│   │   ├── java/com/soat/fiap/videocore/worker/
│   │   │   ├── WorkerApplication.java
│   │   │   ├── core/
│   │   │   │   ├── domain/
│   │   │   │   ├── usecases/
│   │   │   │   └── interfaceadapters/mapper/
│   │   │   └── infrastructure/
│   │   │       ├── in/event/azsvcbus/
│   │   │       │   ├── listener/
│   │   │       │   ├── handler/
│   │   │       │   └── config/
│   │   │       ├── out/
│   │   │       │   ├── process/        # FFmpeg
│   │   │       │   ├── persistence/
│   │   │       │   └── event/azsvcbus/
│   │   │       └── common/
│   │   └── resources/
│   │       ├── application.yaml
│   │       ├── application-local.yaml
│   │       ├── application-prod.yaml
│   │       └── logback-spring.xml
│   └── src/test/
├── docker/
│   └── Dockerfile                # GraalVM + FFmpeg bindings
├── kubernetes/
│   ├── Chart.yaml                # Helm Chart
│   ├── values.yaml               # Configurações Helm
│   └── templates/
│       ├── deploymentset.yaml
│       └── crd/
│           ├── keda/             # ScaledObject + TriggerAuth
│           └── azure_secrets_provider/
├── terraform/
│   ├── main.tf                   # Helm deployment
│   └── variables.tf
└── .github/workflows/
    ├── ci.yaml                   # Build, test, FFmpeg setup
    └── cd.yaml                   # Terraform apply
```

</details>

---

<h2 id="repositorios">📁 Repositórios do Ecossistema</h2>

| Repositório | Responsabilidade | Tecnologias |
|-------------|------------------|-------------|
| **videocore-infra** | Infraestrutura base (AKS, VNET, APIM, Key Vault) | Terraform, Azure, AWS |
| **videocore-db** | Banco de dados | Terraform, Azure Cosmos DB |
| **videocore-frontend** | Interface web do usuário | Next.js 16, React 19, TypeScript |
| **videocore-reports** | Microsserviço de relatórios | Java 25, Spring Boot 4, Cosmos DB |
| **videocore-worker** | Microsserviço de processamento (este repositório) | Java 25, Spring Boot 4, FFmpeg |
| **videocore-observability** | Stack de observabilidade | OpenTelemetry, Jaeger, Prometheus, Grafana |

---

<h2 id="tecnologias">🔧 Tecnologias</h2>

| Categoria | Tecnologia |
|-----------|------------|
| **Linguagem** | Java 25 |
| **Framework** | Spring Boot 4.0.1 |
| **Processamento** | JavaCV, FFmpeg |
| **Mensageria** | Azure Service Bus |
| **Storage** | Azure Blob Storage |
| **Observabilidade** | OpenTelemetry, Micrometer, Logstash |
| **Build** | Gradle |
| **Compilação** | GraalVM Native Image |
| **Container** | Docker |
| **Orquestração** | Kubernetes (Helm), KEDA |
| **IaC** | Terraform |
| **CI/CD** | GitHub Actions |
| **Qualidade** | SonarQube |

---

<h2 id="instalacao">🚀 Instalação e Uso</h2>

### Variáveis de Ambiente

```bash
AZ_SVC_BUS_CONNECTION_STRING=               # Connection string Service Bus
AZURE_BLOB_STORAGE_CONNECTION_STRING=       # Connection string Blob Storage
AZURE_BLOB_STORAGE_VIDEO_CONTAINER_NAME=    # Container de vídeos
AZURE_BLOB_STORAGE_IMAGE_CONTAINER_NAME=    # Container de imagens
```

### Desenvolvimento Local

```bash
# Clonar repositório
git clone https://github.com/FIAP-SOAT-TECH-TEAM/videocore-worker.git
cd videocore-worker/worker

# Configurar variáveis de ambiente
cp env-example .env

# Compilar
./gradlew build

# Executar
./gradlew bootRun

# Executar testes
./gradlew test
```

> ⚠️ O FFmpeg deve estar disponível no sistema para processamento local de vídeos.

---

<h2 id="deploy">⚙️ Fluxo de Deploy</h2>

<details>
<summary>Expandir para mais detalhes</summary>

### Pipeline CI

1. **Build**: JDK 25, Gradle com cache
2. **FFmpeg**: Setup de binários FFmpeg
3. **Testes**: Execução de testes automatizados
4. **Terraform**: Format, validate, plan

### Pipeline CD

1. **Terraform Apply**: Provisionamento via Helm no AKS
2. **Docker**: Build de imagem GraalVM Native Image + FFmpeg
3. **Registry**: Push para Azure Container Registry (ACR)

### Proteções

- Branch `main` protegida
- Nenhum push direto permitido
- Todos os checks devem passar

### Ordem de Provisionamento

```
1. videocore-infra          (AKS, VNET, APIM, Key Vault)
2. videocore-db             (Cosmos DB)
3. videocore-observability  (Jaeger, Prometheus, Grafana)
4. videocore-reports        (Microsserviço de relatórios)
5. videocore-worker         (Este repositório)
6. videocore-frontend       (Interface web)
```

</details>

---

<h2 id="contribuicao">🤝 Contribuição</h2>

### Fluxo de Contribuição

1. Crie uma branch a partir de `main`
2. Implemente suas alterações
3. Execute os testes: `./gradlew test`
4. Abra um Pull Request
5. Aguarde aprovação de um CODEOWNER

### Licença

Este projeto está licenciado sob a [MIT License](LICENSE).

---

<div align="center">
  <strong>FIAP - Pós-graduação em Arquitetura de Software</strong><br>
  Tech Challenge 4
</div>
