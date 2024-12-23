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

APP_PORT=8080

POSTGRES_HOST=postgres
POSTGRES_PORT=5432
POSTGRES_DB=prototype

ADMINER_PORT=8082
```

3. Modify the values as needed for your local setup.
