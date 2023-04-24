package se.callista.blog.management.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.callista.blog.management.domain.entity.Shard;
import se.callista.blog.management.domain.entity.Tenant;
import se.callista.blog.management.repository.SchemaRepository;

@RequiredArgsConstructor
@Slf4j
@Service
public class SchemaManagementServiceImpl implements SchemaManagementService {

    private final SchemaRepository schemaRepository;
    private final SchemaInitializer schemaInitializer;

    @Value("${multitenancy.master.database}")
    private String database;
    @Value("${multitenancy.shard.max-tenants}")
    private int maxTenants;

    private static final String DATABASE_NAME_INFIX = "_shard_";

    @Override
    @Transactional
    public void allocateToSchema(Tenant tenant) {
        List<Shard> shardsWithFreeCapacity = schemaRepository.findShardsWithFreeCapacity(maxTenants);
        if (!shardsWithFreeCapacity.isEmpty()) {
            Shard shard = shardsWithFreeCapacity.get(0);
            shard.addTenant(tenant);
            log.info("Allocated tenant {} to shard {}", tenant.getTenantId(), shard.getDb());
        } else {
            int newSchemaIndex = ((int) schemaRepository.count()) + 1;
            String newShardName = database + DATABASE_NAME_INFIX + newSchemaIndex;
            Shard shard = Shard.builder()
                .id(newSchemaIndex)
                .db(newShardName)
                .build();
            schemaInitializer.initializeDBSchema(shard);
            shard.addTenant(tenant);
            schemaRepository.save(shard);
            log.info("Allocated tenant {} to new shard {}", tenant.getTenantId(), shard.getDb());
        }
    }

}