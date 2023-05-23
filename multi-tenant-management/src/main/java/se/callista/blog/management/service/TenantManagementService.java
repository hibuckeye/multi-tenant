package se.callista.blog.management.service;

import se.callista.blog.management.dto.TenantDTO;

public interface TenantManagementService {

    /**
     * Create a tenant and allocate to a suitable shard.
     */
    void createTenant(String name);

    void createTenant(TenantDTO tenantDTO);

}