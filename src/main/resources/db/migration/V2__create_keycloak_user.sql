CREATE SCHEMA IF NOT EXISTS keycloak;
CREATE USER "keycloak" WITH PASSWORD 'keycloak';
GRANT ALL PRIVILEGES ON SCHEMA keycloak TO "keycloak";