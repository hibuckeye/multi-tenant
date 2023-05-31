# Multi Tenancy with Spring Boot, Hibernate, R2DBC & Liquibase

## How to start a Dockerized postgres database

All the examples require a postgres database running at localhost:5432. Run the following command
to use the provided `docker-compose.yml` configuration to start a dockerized postgres
container:

```
docker-compose up -d
```

Close it with the following command when done, or if you need to recreate the database:

```
docker-compose down
```

