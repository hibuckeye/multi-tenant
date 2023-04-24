package se.callista.blog.management.service;

import se.callista.blog.management.domain.entity.Tenant;

public interface SchemaManagementService {
    
    /**
     * Allocate a tenant to a shard, creating a new shard if necessary.
     * 
     * @param tenant the tenant, which must be a JPA managed object that
     * will be modified by the operation.
     */
    void allocateToSchema(Tenant tenant);

}