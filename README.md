# Camunda External Task Workflow Example
Integration of **Camunda 7**, **Spring Boot External Task Worker**, **Keycloak**, **Circuloos (Orion-LD)** and **Mailpit** for an event-driven human-workflow process.

##  Overview
This project demonstrates an event-driven workflow using Camunda BPM with:
- Secure token-based authentication (Keycloak)
- External task execution (Spring Boot)
- Data enrichment from Circuloos / Orion-LD
- User task completion via Camunda Tasklist
- Email-based human approval loop (Mailpit)

##  Architecture
See diagram in: `src/main/resources/docs/architecture_camunda.md`

### Workflow Summary
1. BPMN deployed via Camunda Modeler
2. Camunda Engine executes workflow → Tasklist UI
3. Spring Boot worker fetches OAuth token from Keycloak
4. Worker queries Circuloos Data Platform
5. Task form is pre-filled in Tasklist
6. Email sent via Mailpit to user
7. User clicks link, completes task
8. Workflow continues in Camunda

##  Components

| Layer | Component | Purpose |
|---|---|---|
| BPMN Design | Camunda Modeler | Process authoring |
| Workflow Engine | Camunda 7 | BPMN runtime |
| User Task UI | Camunda Tasklist | Form interaction |
| Worker | Spring Boot External Task Worker | Executes service tasks |
| Auth | Keycloak | OAuth2 token provider |
| Data Platform | Circuloos Data Platform | Entity data retrieval |
| Notification | Mailpit SMTP | Local email delivery |
| User | Email client + Task UI | Approves/handles tasks |

##  Getting Started

###  Requirements
- Docker & Docker Compose
- Java **17+**
- Maven or Gradle

###  Running

```bash
# Clone the repository
git clone https://github.com/vukeurodyn/camunda-integration-app.git
cd camunda-integration-app

# Start all services using Docker Compose
docker compose up --build -d
```

### Accessing Services

Once all containers are running, you can access:

| Service | URL | Credentials |
|---------|-----|-------------|
| Camunda Cockpit | http://localhost:9091/camunda | demo / demo |
| Camunda Tasklist | http://localhost:9091/camunda/app/tasklist | demo / demo |
| Mailpit Web UI | http://localhost:8025 | - |

### Configuration

The application expects the following environment variables (or `application.yml` configuration):

```yaml
# Camunda
camunda.bpm.client.base-url: http://localhost:9091/engine-rest

# Mail (Mailpit SMTP)
spring.mail.host: localhost
spring.mail.port: 1025
spring.mail.username: 
spring.mail.password:
```

### Deploying the BPMN Process

1. Open **Camunda Modeler**
2. Load `src/main/resources/bpmn/demo.bpmn`
3. Click **Deploy**

### Starting a Process Instance

Go to Camunda Tasklist at **http://localhost:9091/camunda/app/tasklist** and start an instance named _"demo"_.

### Viewing Emails

All emails sent during the workflow can be viewed at: **http://localhost:8025**

### Stopping Services

```bash
docker compose down
```

To remove all data:
```bash
docker compose down -v
```

## Directory Structure

```
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/camunda_integration/
│   │   │       ├── DemoApplication.java
│   │   │       ├── CamundaRestClient.java
│   │   │       ├── ProcessStarterService.java
│   │   │       ├── api/
│   │   │       │   ├── ProcessController.java
│   │   │       │   └── StartRequest.record
│   │   │       ├── vars/
│   │   │       │   └── CamundaVars.java
│   │   │       └── workers/
│   │   │           └── ReviewEmailWorker.java
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── bpmn/
│   │       │   ├── demo.bpmn
│   │       │   └── review.form
│   │       └── docs/
│   │           └── architecture_camunda.md
├── docker-compose.yml
├── Dockerfile
├── pom.xml
└── README.md
```

##  Troubleshooting

### Camunda not accessible
- Verify container is running: `docker ps | grep camunda`
- Check logs: `docker logs camunda`

### Keycloak authentication fails
- Ensure realm and client are configured correctly
- Verify client secret matches configuration

### Emails not sent
- Check Mailpit is running: `docker ps | grep mailpit`
- Verify SMTP configuration points to port 1025

### Worker not fetching tasks
- Check worker is connected: Look for external task subscription logs
- Verify Camunda REST API is accessible from worker

##  License

MIT License

