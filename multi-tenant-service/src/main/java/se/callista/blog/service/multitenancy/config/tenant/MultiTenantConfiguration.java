package se.callista.blog.service.multitenancy.config.tenant;

import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import se.callista.blog.service.multitenancy.datasource.MultiTenantDataSource;
import se.callista.blog.service.multitenancy.repository.TenantRepository;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@Configuration
//@EnableJpaRepositories(
//        basePackages = { "${multitenancy.tenant.repository.packages}" },
//        entityManagerFactoryRef = "tenantEntityManager",
//        transactionManagerRef = "transactionManager")
//@EnableConfigurationProperties(JpaProperties.class)
//@EnableTransactionManagement
public class MultiTenantConfiguration {

//    @Value("${defaultTenant}")
    private String defaultTenant = "tenant_1";


    @Value("${multitenancy.shard.datasource.url-prefix}")
    private String urlPrefix;

    @Value("${multitenancy.shard.username}")
    private String username;

    @Value("${multitenancy.shard.password}")
    private String password;

    @Qualifier("masterDataSource")
    private final DataSource masterDataSource;

    @Qualifier("masterDataSourceProperties")
    private final DataSourceProperties dataSourceProperties;

//    @Value("${multitenancy.tenant.entityManager.packages}")
//    private String entityPackages;

//    private final TenantRepository masterTenantRepository;

//    private final TenantConfiguration tenantConfiguration;

    private static final String TENANT_POOL_NAME_SUFFIX = "_DataSource";

    private static final String SCHEMA_NAME_INFIX = "tenant_";

//    private final List<String> schemas = Arrays.asList("tenant_1", "tenant_2");

//    @Bean(name = "tenantEntityManager")
//    @Primary
//    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(EntityManagerFactoryBuilder builder) {
//        return builder.dataSource(dataSource()).packages(entityPackages).build();
//    }
//
//    @Primary
//    @Bean(name = "transactionManager")
//    public JpaTransactionManager transactionManager(
//            @Autowired @Qualifier("tenantEntityManager") LocalContainerEntityManagerFactoryBean entityManagerFactoryBean) {
//        return new JpaTransactionManager(entityManagerFactoryBean.getObject());
//    }

    @Bean("tenantDataSource")
    @Primary
//    @ConfigurationProperties(prefix = "tenants")
    public DataSource dataSource() {
//        File[] files = Paths.get("allTenants").toFile().listFiles();
        Map<Object, Object> resolvedDataSources = new HashMap<>();

        List<String> schemas = getDbsFromTenant();
//        System.out.println(dbs);

//        for (File propertyFile : files) {
//            Properties tenantProperties = new Properties();
//            DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
//
//            try {
//                tenantProperties.load(new FileInputStream(propertyFile));
//                String tenantId = tenantProperties.getProperty("name");
//
//                dataSourceBuilder.driverClassName(tenantProperties.getProperty("datasource.driver-class-name"));
//                dataSourceBuilder.username(tenantProperties.getProperty("datasource.username"));
//                dataSourceBuilder.password(tenantProperties.getProperty("datasource.password"));
//                dataSourceBuilder.url(tenantProperties.getProperty("datasource.url"));
//                resolvedDataSources.put(tenantId, dataSourceBuilder.build());
//            } catch (IOException exp) {
//                throw new RuntimeException("Problem in tenant datasource:" + exp);
//            }
//        }

        for (String schema: schemas) {
            DataSource ds = createAndConfigureDataSource(schema);
            resolvedDataSources.put(schema, ds);
        }

        AbstractRoutingDataSource dataSource = new MultiTenantDataSource(masterDataSource, dataSourceProperties, resolvedDataSources);
//        dataSource.setDefaultTargetDataSource(resolvedDataSources.get(defaultTenant));
//        dataSource.setTargetDataSources(resolvedDataSources);
//        dataSource.afterPropertiesSet();

        return dataSource;
    }

    private DataSource createAndConfigureDataSource(String schema) {
        HikariDataSource ds = dataSourceProperties.initializeDataSourceBuilder()
                .type(HikariDataSource.class).build();

        ds.setUsername(username);
        ds.setPassword(password);
        ds.setJdbcUrl(urlPrefix + schema);

        ds.setPoolName(schema + TENANT_POOL_NAME_SUFFIX);

        log.info("Configured datasource: {}", ds.getPoolName());
        return ds;
    }

    public List<String> getDbsFromTenant() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(masterDataSource);
        String sql = "SELECT db FROM tenant";
        return jdbcTemplate.queryForList(sql, String.class);
    }

}
