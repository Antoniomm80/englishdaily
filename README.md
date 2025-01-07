# English Daily Lessons

A simple application that randomly gets vocabulary and grammar lessons from a set of PDFs by doing RAG

Just for the sake of fiddling around with Spring AI and Ollama

# Architecture

Spring Boot 3.4 + Spring AI for backend

Ollama for running models locally

- Nomic Embed Text for embedding generation
- Llama 3.2:3b for chat

PGVector database running in a Raspberry-Pi powered K8s cluster for storing PDFs embeddings

# Help

## Docker

Maven command for building docker image with spring boot plugin
`mvn spring-boot:build-image -Dspring-boot.build-image.imageName=192.168.4.30:5000/englishdaily:latest`

## PgVector

### Postgres client management in Kubernetes

For opening a client in the pgvector running pod

`k -n db exec pod/pgvector-POD_INSTANCE -it -- psql -U postgres`

For running a temporary pod running Postgres client

In the db namespace, a deployment or pod must have the label role=db-job to access any of the running databases, as specified by the applied network
policies.

`k -n db run tmp -it --rm --restart Never --labels=role=db-job --image postgres:16 --env="P
GPASSWORD=WCP4P9pbEnVNuQAALfB4" -- psql -h pgvector -U postgres`

### Useful meta-commands

- `\l` Equivalent to MySql SHOW DATABASES
- `\dt schema_name.*` Equivalent to MySql SHOW TABLES from schema_name database
