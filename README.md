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
| Data Platform | Circuloos / Orion-LD | Entity data retrieval |
| Notification | Mailpit SMTP | Local email delivery |
| User | Email client + Task UI | Approves/handles tasks |

##  Getting Started

###  Requirements
- Docker & Docker Compose
- Java **17+**
- Maven or Gradle

###  Running
```bash
git clone https://github.com/your-org/your-repo.git
cd your-repo
```
docker run -p 8025:8025 -p 1025:1025 axllent/mailpit
docker run -d --name camunda -p 9090:9090 camunda/camunda-bpm-platform:latest
### Directory Structure

```
├── resources/
│   ├── docs/
│   │   └── architecture_camunda.md
│   └── bpmn/
│       ├── demo.bpmn
│       └── review.form
└── README.md
```