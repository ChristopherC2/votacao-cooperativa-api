# 🗳️ Sistema de Votação Cooperativa

API REST desenvolvida em **Java 17 + Spring Boot 3** para gerenciamento de pautas e votação em sessões abertas, com integração externa de validação de CPF e mensageria assíncrona utilizando **RabbitMQ**.

---

## 🚀 Tecnologias Utilizadas

* Java 17
* Spring Boot 3
* Spring Data JPA
* PostgreSQL
* RabbitMQ
* Docker
* Mockito
* JUnit 4
* Maven

---

## 🧱 Arquitetura

A aplicação segue os princípios de **Clean Architecture**, separando responsabilidades entre:

```
controller → service → repository → domain
                     ↓
               messaging
                     ↓
              external client
```

* **Controller:** Camada de entrada HTTP
* **Service:** Regras de negócio
* **Repository:** Persistência
* **Messaging:** Publicação de eventos
* **Client:** Integração com API externa de CPF

---

## 📌 Funcionalidades

* Criar pauta
* Abrir sessão de votação (com tempo configurável)
* Registrar voto
* Impedir votos duplicados por CPF
* Validação de CPF via API externa
* Encerramento automático da sessão
* Cálculo automático do resultado
* Publicação de eventos em fila

---

## 🔗 Integração Externa

Antes de registrar o voto, o sistema consulta uma API externa para validar se o CPF está apto a votar.

Exemplo de resposta esperada:

```json
{
  "status": "ABLE_TO_VOTE"
}
```

---

## 🐇 Mensageria

A aplicação publica eventos no RabbitMQ:

### ✔️ Quando um voto é registrado

Evento:

```
vote.created
```

Payload:

```json
{
  "agendaId": "uuid",
  "cpf": "string",
  "voteValue": "YES | NO"
}
```

---

### ✔️ Quando uma sessão é encerrada automaticamente

Evento:

```
votacao.result
```

Payload:

```json
{
  "agendaId": "uuid",
  "yesVotes": 10,
  "noVotes": 3
}
```

---

## ⏱️ Scheduler

Um processo agendado verifica periodicamente sessões expiradas:

* Encerra sessões abertas
* Calcula resultado
* Publica `VoteResultEvent` na fila

---

## 🐘 Banco de Dados

Subir PostgreSQL via Docker:

```bash
docker run -d -p 5432:5432 \
-e POSTGRES_DB=votacao \
-e POSTGRES_USER=postgres \
-e POSTGRES_PASSWORD=postgres \
--name votacao-db postgres
```

---

## ⚙️ Configuração

### application.yml

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/votacao
    username: postgres
    password: postgres
    driver-class-name: org.postgresql.Driver

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
```

---

## ▶️ Executando o Projeto

```bash
./mvnw spring-boot:run
```

A aplicação será iniciada em:

```
http://localhost:8080
```

---

## 📬 Endpoints

### Criar Pauta

```
POST /api/v1/agendas
Content-Type: text/plain
```

Body:

```
Nova pauta
```

---

### Abrir Sessão

```
POST /api/v1/sessions/{agendaId}?minutes=1
```

---

### Registrar Voto

```
POST /api/v1/votes
Content-Type: application/json
```

Body:

```json
{
  "agendaId": "uuid",
  "cpf": "12345678900",
  "vote": "YES"
}
```

---

### Consultar Resultado

```
GET /api/v1/votes/result/{agendaId}
```

---

## 🧪 Testes

Testes unitários implementados com:

* JUnit 4
* Mockito
* Spring WebMvcTest

Executar:

```bash
./mvnw test
```

---

## ☁️ Deploy

A aplicação pode ser publicada em plataformas como:

* Render
* Railway
* Heroku

Avaliadores podem acessar o repositório público para análise de código.

---

## 👨‍💻 Autor

Christopher Castro
