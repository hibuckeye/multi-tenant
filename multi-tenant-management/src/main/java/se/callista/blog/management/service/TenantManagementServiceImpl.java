package se.callista.blog.management.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.callista.blog.management.domain.entity.Tenant;

@RequiredArgsConstructor
@Service
public class TenantManagementServiceImpl implements TenantManagementService {

    private final SchemaManagementService schemaManagementService;

    @Override
    @Transactional
    public void createTenant(String tenantId) {
        Tenant tenant = Tenant.builder()
                .tenantId(tenantId)
                .build();
        schemaManagementService.allocateToSchema(tenant);
    }

}
