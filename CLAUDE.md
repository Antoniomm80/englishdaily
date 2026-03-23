# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Build
./mvnw clean package

# Run all tests
./mvnw test

# Run a single test class
./mvnw test -Dtest=ClassName

# Run a single test method
./mvnw test -Dtest=ClassName#methodName

# Build Docker image and deploy to K8s
./deploy.sh
```

The deploy script builds the Docker image using the Spring Boot Maven plugin (targets the private registry at `192.168.4.30:5000`), pushes it, and rolls out the new image to the `projects` namespace in the Kubernetes cluster.

## Architecture

Spring Boot 3.4 + Spring AI 1.0 application that generates daily English vocabulary and grammar lessons via RAG over PDF documents stored in Google Drive.

### Core Flow

1. **Ingestion**: `IngestionController` → `IngestionPipeline` reads PDFs from Google Drive (or classpath fallback via `FolderReader` implementations), embeds them with Ollama (`nomic-embed-text`), and stores chunks in PGVector. Skips files already present in the vector store.

2. **Daily Vocabulary**: `VocabularyLessonCreator` runs at 7 AM daily via `@Scheduled`. It calls `VocabularyService` N times (configured by `vocabulary-lessons.max-size`), passing a blacklist of already-chosen words to avoid repetition. Each call uses `QuestionAnswerAdvisor` to retrieve relevant chunks from PGVector filtered by a randomly selected document filename, then prompts the LLM to extract one vocabulary term. Results in a `VocabularyLessonCreated` Spring application event.

3. **Event Broadcasting**: `VocabularyLessonEventBroadcaster` listens for `VocabularyLessonCreated` events and publishes them as JSON to a RabbitMQ fanout exchange (`exchange.home.events`).

4. **WebSocket Chat**: `askllama` package exposes a Socket.IO server (port 8878) for real-time LLM chat via `AskLlmService`.

### Package Structure

- `vocabulary` — Core domain: lesson generation, event model, RabbitMQ broadcast, markdown formatting
- `grammar` — Grammar lesson generation (OpenAI and DeepSeek implementations)
- `catalog` — JPA entities and repositories for `Course` and `GrammarLesson`
- `vectorstore` — Ingestion pipeline, PGVector repository, document enrichment
- `documentreader` — `FolderReader` abstraction with Google Drive and classpath implementations
- `askllama` — Socket.IO WebSocket server and LLM chat service
- `chatclient` — Spring AI `ChatClient` bean configuration

### LLM Service Implementations

`VocabularyService` and `GrammarService` each have multiple implementations (`OpenAi*`, `DeepSeek*`, `Llama*`). The active implementation is selected via Spring configuration/conditionals. The active chat model is controlled by `englishdaily.chat-service.llm-model` and `spring.ai.model.chat`.

### Testing

Integration tests use `@IntegrationTest` (a composed annotation). It:
- Spins up a `pgvector/pgvector:pg16` Testcontainer (with reuse enabled)
- Spins up a `rabbitmq:3.12-management` Testcontainer via `RabbitMqTCInitializer`
- Mocks `SocketIOServer` and `BroadcastOperations` via `TestEnvironmentConfiguration`
- Uses OpenAI for the chat model (key configured in `src/test/resources/application.properties`)

Test embedding model differs from production: tests use Ollama (`nomic-embed-text`, 768 dimensions) while production uses OpenAI embeddings (1536 dimensions).

The `mock-in-bean` library is used for replacing beans within the running Spring context without restarting it.

### Infrastructure

- PGVector (PostgreSQL + pgvector extension) running on a Raspberry Pi K8s cluster in the `db` namespace
- RabbitMQ at `rabbitmq.broker`
- Ollama at `http://192.168.5.168:11434`
- Liquibase manages schema migrations under `src/main/resources/db/changelog/`
