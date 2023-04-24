package se.callista.blog.service.multitenancy.config.shard;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import se.callista.blog.service.multitenancy.config.shard.liquibase.DynamicSchemaMultiTenantSpringLiquibase;
import se.callista.blog.service.multitenancy.repository.SchemaRepository;

@Configuration
@ConditionalOnProperty(name = "multitenancy.shard.liquibase.enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(LiquibaseProperties.class)
public class ShardLiquibaseConfig {

    @Bean
    @ConfigurationProperties("multitenancy.shard.liquibase")
    public LiquibaseProperties shardLiquibaseProperties() {
        return new LiquibaseProperties();
    }

    @Bean
    public DynamicSchemaMultiTenantSpringLiquibase shardLiquibase(
        SchemaRepository schemaRepository,
        @Qualifier("shardLiquibaseProperties") LiquibaseProperties liquibaseProperties) {
        return new DynamicSchemaMultiTenantSpringLiquibase(schemaRepository, liquibaseProperties);
    }

}
