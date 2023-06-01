//package se.callista.blog.service.multitenancy.config.shard.liquibase;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//import javax.sql.DataSource;
//import liquibase.exception.LiquibaseException;
//import liquibase.integration.spring.SpringLiquibase;
//import lombok.Getter;
//import lombok.RequiredArgsConstructor;
//import lombok.Setter;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
//import org.springframework.context.ResourceLoaderAware;
//import org.springframework.core.io.ResourceLoader;
//import org.springframework.jdbc.datasource.SingleConnectionDataSource;
//import se.callista.blog.service.multitenancy.domain.entity.Tenant;
//import se.callista.blog.service.multitenancy.repository.TenantRepository;
//
///**
// * Based on MultiTenantSpringLiquibase, this class provides Liquibase support for
// * Sharding based on a dynamic collection of DataSources.
// */
//@RequiredArgsConstructor
//@Getter
//@Setter
//@Slf4j
//public class DynamicSchemaMultiTenantSpringLiquibase implements InitializingBean, ResourceLoaderAware {
//
//    private final TenantRepository tenantRepository;
//
//    @Qualifier("schemaLiquibaseProperties")
//    private final LiquibaseProperties liquibaseProperties;
//
//    @Value("${multitenancy.shard.datasource.url-prefix}")
//    private String urlPrefix;
//    @Value("${multitenancy.master.datasource.username}")
//    private String username;
//    @Value("${multitenancy.master.datasource.password}")
//    private String password;
//
//    private ResourceLoader resourceLoader;
//
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        this.runOnAllSchemas(tenantRepository.findAll());
//    }
//
//    protected void runOnAllSchemas(Iterable<Tenant> tenants) { //for each schema, get connection and run liquibase
//        for(Tenant tenant : tenants) {
//            String schema = tenant.getDb();
//            log.info("Initializing Liquibase for tenant(schema): " + schema);
//            try (Connection connection = DriverManager.getConnection(urlPrefix + schema, username, password)) {
//                DataSource shardDataSource = new SingleConnectionDataSource(connection, false);
//                SpringLiquibase liquibase = this.getSpringLiquibase(shardDataSource);
//                liquibase.afterPropertiesSet();
//            } catch (SQLException | LiquibaseException e) {
//                log.error("Failed to run Liquibase for tenant(schema): " +schema, e);
//            }
//            log.info("Liquibase successful ran for tenant(schema): " + schema);
//        }
//    }
//
//    protected SpringLiquibase getSpringLiquibase(DataSource dataSource) { //run liquibase dbchangelog
//        SpringLiquibase liquibase = new SpringLiquibase();
//        liquibase.setResourceLoader(getResourceLoader());
//        liquibase.setDataSource(dataSource);
//        liquibase.setChangeLog(liquibaseProperties.getChangeLog());
//        liquibase.setContexts(liquibaseProperties.getContexts());
//        liquibase.setLiquibaseSchema(liquibaseProperties.getLiquibaseSchema());
//        liquibase.setLiquibaseTablespace(liquibaseProperties.getLiquibaseTablespace());
//        liquibase.setDatabaseChangeLogTable(liquibaseProperties.getDatabaseChangeLogTable());
//        liquibase.setDatabaseChangeLogLockTable(liquibaseProperties.getDatabaseChangeLogLockTable());
//        liquibase.setDropFirst(liquibaseProperties.isDropFirst());
//        liquibase.setShouldRun(liquibaseProperties.isEnabled());
//        liquibase.setLabels(liquibaseProperties.getLabels());
//        liquibase.setChangeLogParameters(liquibaseProperties.getParameters());
//        liquibase.setRollbackFile(liquibaseProperties.getRollbackFile());
//        liquibase.setTestRollbackOnUpdate(liquibaseProperties.isTestRollbackOnUpdate());
//        return liquibase;
//    }
//
//}
