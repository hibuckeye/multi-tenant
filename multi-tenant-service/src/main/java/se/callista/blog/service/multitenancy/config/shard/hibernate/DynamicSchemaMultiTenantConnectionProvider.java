//package se.callista.blog.service.multitenancy.config.shard.hibernate;
//
//import com.github.benmanes.caffeine.cache.Caffeine;
//import com.github.benmanes.caffeine.cache.LoadingCache;
//import com.github.benmanes.caffeine.cache.RemovalListener;
//import com.zaxxer.hikari.HikariDataSource;
//import java.util.concurrent.TimeUnit;
//import javax.annotation.PostConstruct;
//import javax.sql.DataSource;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
//import org.springframework.stereotype.Component;
//import se.callista.blog.service.exception.NoSuchTenantException;
////import se.callista.blog.service.multitenancy.datasource.TenantAwareDataSource;
////import se.callista.blog.service.multitenancy.domain.entity.Shard;
//import se.callista.blog.service.multitenancy.domain.entity.Tenant;
//import se.callista.blog.service.multitenancy.repository.TenantRepository;
//
//@RequiredArgsConstructor
//@Slf4j
//@Component
//public class DynamicSchemaMultiTenantConnectionProvider
//                extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {
//
//    private static final long serialVersionUID = -460277105706399638L;
//
//    private static final String TENANT_POOL_NAME_SUFFIX = "_DataSource";
//
//    private static final String SCHEMA_NAME_INFIX = "tenant_";
//
//    @Qualifier("masterDataSource")
//    private final DataSource masterDataSource;
//
//    @Qualifier("masterDataSourceProperties")
//    private final DataSourceProperties dataSourceProperties;
//
//    private final TenantRepository masterTenantRepository;
//
//    @Value("${multitenancy.tenant-cache.maximumSize:100}")
//    private Long tenantCacheMaximumSize;
//
//    @Value("${multitenancy.tenant-cache.expireAfterAccess:10}")
//    private Integer tenantCacheExpireAfterAccess;
//
//    @Value("${multitenancy.datasource-cache.maximumSize:#{null}}")
//    private Long datasourceCacheMaximumSize;
//
//    @Value("${multitenancy.datasource-cache.expireAfterAccess:#{null}}")
//    private Integer datasourceCacheExpireAfterAccess;
//
//    @Value("${multitenancy.shard.datasource.url-prefix}")
//    private String urlPrefix;
//
//    @Value("${multitenancy.shard.username}")
//    private String username;
//
//    @Value("${multitenancy.shard.password}")
//    private String password;
//
////    private LoadingCache<String, Tenant> tenants;
//
//    private LoadingCache<String, DataSource> schemaDataSources;
//
//    @Override
//    protected DataSource selectAnyDataSource() {
//        return masterDataSource;
//    }
//
//    @Override
//    protected DataSource selectDataSource(String tenantIdentifier) {
////        Tenant tenant = tenants.get(tenantIdentifier);
//        DataSource schemaDataSource = schemaDataSources.get(SCHEMA_NAME_INFIX + tenantIdentifier);
////        return new TenantAwareDataSource(schemaDataSource);
//        return schemaDataSource;
//    }
//
//    @PostConstruct
//    private void createCaches() {
////        //cache tenant <String, Tenant>
////        Caffeine<Object, Object> tenantsCacheBuilder = Caffeine.newBuilder();
////        if (tenantCacheMaximumSize != null) {
////            tenantsCacheBuilder.maximumSize(tenantCacheMaximumSize);
////        }
////        if (tenantCacheExpireAfterAccess != null) {
////            tenantsCacheBuilder.expireAfterAccess(tenantCacheExpireAfterAccess, TimeUnit.MINUTES);
////        }
////        tenants = tenantsCacheBuilder.build(
////            tenantId -> masterTenantRepository.findByTenantId(tenantId).orElseThrow(
////                            () -> new NoSuchTenantException("No such tenant: " + tenantId)));
//
//        //cache scheme <Shard, DataSource>
//        Caffeine<Object, Object> schemaDataSourcesCacheBuilder = Caffeine.newBuilder();
//        if (datasourceCacheMaximumSize != null) {
//            schemaDataSourcesCacheBuilder.maximumSize(datasourceCacheMaximumSize);
//        }
//        if (datasourceCacheExpireAfterAccess != null) {
//            schemaDataSourcesCacheBuilder.expireAfterAccess(datasourceCacheExpireAfterAccess,
//                            TimeUnit.MINUTES);
//        }
//        schemaDataSourcesCacheBuilder.removalListener(
//            (RemovalListener<String, DataSource>) (schema, dataSource, removalCause) -> {
//                    HikariDataSource ds = (HikariDataSource) dataSource;
//                    ds.close(); // tear down properly
//                    log.info("Closed datasource: {}", ds.getPoolName());
//            });
//        schemaDataSources = schemaDataSourcesCacheBuilder.build(
//            schema -> createAndConfigureDataSource(schema));
//    }
//
//    private DataSource createAndConfigureDataSource(String schema) {
//        HikariDataSource ds = dataSourceProperties.initializeDataSourceBuilder()
//                        .type(HikariDataSource.class).build();
//
//        ds.setUsername(username);
//        ds.setPassword(password);
//        ds.setJdbcUrl(urlPrefix + schema);
//
//        ds.setPoolName(schema + TENANT_POOL_NAME_SUFFIX);
//
//        log.info("Configured datasource: {}", ds.getPoolName());
//        return ds;
//    }
//
//}
