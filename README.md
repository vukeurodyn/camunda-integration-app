# CIRCULOOS Plastic Pilot
This Spring Boot service implements the material-requirement and stock-validation logic used in the CIRCULOOS Plastic Pilot
##  Overview
It integrates with the Orion-LD Digital Twin to determine:

- How much material is required to manufacture a batch of plastic assemblies (e.g., washing-machine trays)
- How many units can actually be produced based on current component stock
- Whether production is possible or insufficient
- Tasklist manages human tasks
- Mailpit simulates notification emails

It exposes two REST endpoints used by Camunda and by Postman for testing.

## Endpoints Overview

Triggered by Camunda (Service Task 1).
##  1. POST /api/material/calculate
```bash
{
  "manufacturingMachineId": "urn:ngsi-ld:ManufacturingMachine:washingMachineTray:001",
  "requiredUnits": 100
}
```
What it does:

### 1. Fetches the ManufacturingMachine entity from Orion-LD
- Uses header: ```NGSILD-Tenant: circuloos_demo```
### 2. Reads
- ```materialTotals``` (total kg per material for 1 tray)
- ```hasComponent```(component IDs)
### 3. Calulates required kg per material for the batch (unit x quantity)
### 4. Stores
- material requirements
- component IDs
- requested quantity into an internal cache using ```calculationId```
### 5. returns minimal response to workflow
```json 
{
  "calculationId": "74205606-c5f4-40b7-88d6-7b9964d9b0f8",
  "machineId": "urn:ngsi-ld:ManufacturingMachine:washingMachineTray:001",
  "requestedUnits": 100
}
```
## 2. GET /api/material/validate?calcId=...
Triggered by Camunda (Service Task 2).

### 1. Loads previously stored calculation data (material requirements + component IDs)
### 2. For each component
- Fetches component DT from Orion-LD
- Reads ``stocklevel, weight.value, hasMaterial.object``
### 3. Converts component stock → material stock in kg, grouped by materialId
### 4. Compares
- ```availableKg``` vs ```requiredKg```
### 5. Returns
- whether enough material exists
- max number of units that can be produced with current inventory

Response example:
```json
{
"sufficient": false,
"maxProducibleUnits": 47
}
```
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

| Service          | URL                                        | Credentials |
|------------------|--------------------------------------------|-------------|
| Camunda Cockpit  | http://localhost:9091/camunda              | demo / demo |
| Camunda Tasklist | http://localhost:9091/camunda/app/tasklist | demo / demo |
| Mailpit Web UI   | http://localhost:8025                      | - |
| worker           | http://localhost:9092                      | - |
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
2. Load `src/main/resources/bpmn/plastic_demo.bpmn`
3. Include forms (or not) 
4. Click **Deploy**

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

