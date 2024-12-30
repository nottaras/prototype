# Prototype

[![CI](https://github.com/nottaras/prototype/actions/workflows/ci.yml/badge.svg?branch=main)](https://github.com/nottaras/prototype/actions/workflows/ci.yml)

## Environment Configuration

The project requires a .env file for local development. This file stores environment-specific variables, such as
database credentials and application configuration.

### Setting Up the .env File

1. Create a .env file in the root directory of the project.
2. Copy and paste the following properties into the .env file:

```
SPRING_APPLICATION_NAME=prototype

APP_PORT=8080
APP_DB: prototype
APP_DB_SCHEMA: app
APP_DB_USER: prototype
APP_DB_PASSWORD: prototype

SPRING_JPA_SHOW_SQL=true
SPRING_JPA_PROPERTIES_HIBERNATE_FORMAT_SQL=true

POSTGRES_HOST=postgres
POSTGRES_PORT=5432
POSTGRES_DB=prototype
POSTGRES_USERN=postgres
POSTGRES_PASSWORD=postgres

FLYWAY_DB_USER: flyway
FLYWAY_DB_PASSWORD: flyway

ADMINER_PORT=8081

KEYCLOAK_PORT=8082
KEYCLOAK_ADMIN=admin
KEYCLOAK_ADMIN_PASSWORD=admin
KC_DB=postgres
KEYCLOAK_DB_SCHEMA=keycloak
KEYCLOAK_DB_USER=keycloak
KEYCLOAK_DB_PASSWORD=keycloak
KEYCLOAK_CLIENT_ID=prototype

PROMETHEUS_PORT=9090
GRAFANA_PORT=3000

MONGO_PORT=27017
MONGO_EXPRESS_PORT=8083
MONGO_HOST=mongo
MONGO_DB=prototype
MONGO_USER=mongo
MONGO_PASSWORD=mongo
FILE_UPLOAD_MAX_SIZE=15728640
FILE_UPLOAD_ALLOWED_TYPES=image/jpeg,image/png
```

3. Modify the values as needed for your local setup.

## Local Development Resources

- [Application](http://localhost:8080) - Main application.
- [Swagger](http://localhost:8080/swagger-ui/index.html#) - API documentation for testing and exploration.
- [Adminer](http://localhost:8081) - Database management interface.
- [Keycloak](http://localhost:8082) - Admin panel for authentication and authorization.
- [Prometheus](http://localhost:9090) - Metrics and monitoring system.
- [Grafana](http://localhost:3000) - Visualization and dashboarding for metrics.
- [MongoDB](http://localhost:27017) - NoSQL database.
- [Mongo Express](http://localhost:8083) - Web-based MongoDB administration interface.

## Obtaining the Access Token

To obtain an **access token**, you need to send a request to the Keycloak endpoint using the `password` grant type to
get a token via `username` and `password`. Use the following `curl` command:

```bash
curl --location 'http://localhost:8082/realms/prototype/protocol/openid-connect/token' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'grant_type=password' \
--data-urlencode 'client_id=prototype-app' \
--data-urlencode 'username=user@gmail.com' \
--data-urlencode 'password=123456' | jq -r '.access_token'
```
