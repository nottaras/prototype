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
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=postgres
SPRING_JPA_SHOW_SQL=true
SPRING_JPA_PROPERTIES_HIBERNATE_FORMAT_SQL=true

APP_SERVER_PORT=8080

POSTGRES_HOST=postgres
POSTGRES_PORT=5432
POSTGRES_DB=prototype

ADMINER_PORT=8081

KEYCLOAK_PORT=8082
KEYCLOAK_ADMIN=admin
KEYCLOAK_ADMIN_PASSWORD=admin
KC_DB=postgres
KC_DB_SCHEMA=keycloak
KC_DB_USERNAME=keycloak
KC_DB_PASSWORD=keycloak
KEYCLOAK_CLIENT_ID=prototype
```

3. Modify the values as needed for your local setup.

## Local Development Resources

- [Application](http://localhost:8080) - Main application.
- [Swagger](http://localhost:8080/swagger-ui/index.html#) - API documentation for testing and exploration.
- [Adminer](http://localhost:8081) - Database management interface.
- [Keycloak](http://localhost:8082) - Admin panel for authentication and authorization.

## Obtaining the Access Token

To obtain an **access token**, you need to send a request to the Keycloak endpoint using the `password` grant type to
get a token via `username` and `password`. Use the following `curl` command:

```bash
curl --location 'http://localhost:8082/realms/prototype/protocol/openid-connect/token' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'grant_type=password' \
--data-urlencode 'client_id=prototype' \
--data-urlencode 'username=foo.bar@gmail.com' \
--data-urlencode 'password=123456'
```
