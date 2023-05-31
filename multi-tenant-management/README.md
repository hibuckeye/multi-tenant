# Multi Tenant Management

## Overview  

This app provides a simple rest interface for dynamically creating new tenants

## Running the Multi Tenant Management Service

Build the Multi Tenant Management executable:

```
mvn package
```

then start it as an simple java application:

```
java -jar target/multi-tenant-management-0-SNAPSHOT.jar
```
or via maven
```
mvn spring-boot:run
```

## Testing the Multi Tenant Management Service

Set up some different tenants (Create new tenants):

```
curl --location 'localhost:8088/tenants' --header 'Content-Type: application/json' --data '{"name": "test tenant1"}'
curl --location 'localhost:8088/tenants' --header 'Content-Type: application/json' --data '{"name": "test tenant2"}'
```

## Configuration

Change default port value and other settings in src/main/resources/application.yml.
