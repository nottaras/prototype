#!/bin/bash
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<EOSQL
  CREATE DATABASE "$APP_DB" WITH ENCODING 'UTF8';

  \connect "$APP_DB"

  CREATE SCHEMA IF NOT EXISTS "$APP_DB_SCHEMA";

  CREATE USER "$FLYWAY_DB_USER" WITH PASSWORD '$FLYWAY_DB_PASSWORD';
  GRANT ALL PRIVILEGES ON SCHEMA "$APP_DB_SCHEMA" TO "$FLYWAY_DB_USER" WITH GRANT OPTION;
  ALTER USER "$FLYWAY_DB_USER" SET SEARCH_PATH = '$APP_DB_SCHEMA';

  CREATE USER "$APP_DB_USER" WITH PASSWORD '$APP_DB_PASSWORD';
  GRANT USAGE ON SCHEMA "$APP_DB_SCHEMA" TO "$APP_DB_USER";
  ALTER USER "$APP_DB_USER" SET SEARCH_PATH = '$APP_DB_SCHEMA';

  CREATE SCHEMA IF NOT EXISTS "$KEYCLOAK_DB_SCHEMA";
  CREATE USER "$KEYCLOAK_DB_USER" WITH PASSWORD '$KEYCLOAK_DB_PASSWORD';
  GRANT ALL PRIVILEGES ON SCHEMA "$KEYCLOAK_DB_SCHEMA" TO "$KEYCLOAK_DB_USER";
EOSQL