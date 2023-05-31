# Multi Tenant Service Demo

## Running the Multi Tenant Service

Build the Multi Tenant Service executable:

```
mvn package
```

then start it as an simple java application:

```
java -jar target/multi-tenant-service-0-SNAPSHOT.jar
```
or via maven
```
mvn spring-boot:run
```

## Testing the Multi Tenant Service

Add some data to different tenant by specifying tenantId in header

```
curl --location 'localhost:8080/products' --header 'X-TENANT-ID: 1' --header 'Content-Type: application/se.callista.blog.service.api.product.v1_0+json' --data '{"name": "Product 111"}'
curl --location 'localhost:8080/products' --header 'X-TENANT-ID: 2' --header 'Content-Type: application/se.callista.blog.service.api.product.v1_0+json' --data '{"name": "Product 2"}'
```

Then query for the data, and verify that the data is properly isolated between tenants:

```
curl --location 'localhost:8080/products' --header 'X-TENANT-ID: 1'
curl --location 'localhost:8080/products' --header 'X-TENANT-ID: 2'
```

## Configuration

Change default port value and other settings in src/main/resources/application.yml.
