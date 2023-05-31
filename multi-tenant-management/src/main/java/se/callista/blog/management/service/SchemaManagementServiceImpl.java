package se.callista.blog.management.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.callista.blog.management.domain.entity.Tenant;
import se.callista.blog.management.repository.TenantRepository;

@RequiredArgsConstructor
@Slf4j
@Service
public class SchemaManagementServiceImpl implements SchemaManagementService {

    private final TenantRepository tenantRepository;
    private final SchemaInitializer schemaInitializer;

    private static final String SCHEMA_NAME_INFIX = "tenant_";

    @Override
    @Transactional
    public void allocateToSchema(Tenant tenant) {
            String schemaName = SCHEMA_NAME_INFIX + tenant.getTenantId();
            schemaInitializer.initializeDBSchema(schemaName);
            tenant.setDb(schemaName);
            tenantRepository.save(tenant);
            log.info("Allocated tenant {} to new schema {}", tenant.getTenantId(), schemaName);
//        }
    }

}
