package se.callista.blog.management.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.callista.blog.management.domain.entity.Tenant;
import se.callista.blog.management.dto.TenantDTO;
import se.callista.blog.management.repository.TenantRepository;

@RequiredArgsConstructor
@Service
public class TenantManagementServiceImpl implements TenantManagementService {

    private final TenantRepository tenantRepository;

    private final SchemaManagementService schemaManagementService;

    @Override
    @Transactional
    public void createTenant(String name) {
        Tenant tenant = Tenant.builder()
                .name(name)
//                .tenantId(tenantId)
                .build();
        tenantRepository.save(tenant);
        schemaManagementService.allocateToSchema(tenant);
    }

    @Override
    @Transactional
    public void createTenant(TenantDTO tenantDTO) {
        Tenant tenant = Tenant.builder()
                .name(tenantDTO.getName())
//                .tenantId(tenantId)
                .build();
        tenantRepository.save(tenant);
        schemaManagementService.allocateToSchema(tenant);
    }

}
