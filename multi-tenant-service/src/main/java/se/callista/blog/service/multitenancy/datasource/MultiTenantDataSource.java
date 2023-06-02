package se.callista.blog.service.multitenancy.datasource;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import se.callista.blog.service.multitenancy.util.TenantContext;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Tenant-Aware Datasource that decorates Connections with
 * current tenant information.
 */
public class MultiTenantDataSource extends AbstractRoutingDataSource {

    private final ConcurrentHashMap<Object, Object> dataSourceMap;

    private static final String TENANT_POOL_NAME_SUFFIX = "_DataSource";

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

    public MultiTenantDataSource(DataSource masterDataSource, DataSourceProperties dataSourceProperties, Map<Object, Object> targetDataSources) {
//        super.setDefaultTargetDataSource(defaultTargetDataSource);
        this.masterDataSource = masterDataSource;
        this.dataSourceProperties = dataSourceProperties;
        this.dataSourceMap = new ConcurrentHashMap<>(targetDataSources);
        super.setTargetDataSources(this.dataSourceMap);
        super.afterPropertiesSet();
    }

    public synchronized void addDataSource(String key, DataSource dataSource) {
        Optional<Long> id = getDBConfigByKey(key);
        dataSourceMap.put(key, dataSource);
        // 必须再次调用此方法，因为它会重新构造 resolvedDataSources，并重置 resolvedDefaultDataSource
        super.afterPropertiesSet();
    }

    private Optional<Long> getDBConfigByKey(String key) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(masterDataSource);


//        String sql = "SELECT * FROM tenant WHERE id = ?";
//        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
//            // 根据需要映射行数据到Tenant对象中
//            Tenant tenant = new Tenant();
//            tenant.setId(rs.getInt("id"));
//            tenant.setName(rs.getString("name"));
//            // 其他属性
//            return tenant;
//        });


        String sql = "SELECT tenant_id FROM tenant WHERE db = ?";
//        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new Object[]{key}, Long.class));
        List<Long> names = jdbcTemplate.query(sql, new Object[]{key}, new NameRowMapper());
        if (names.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(names.get(0));
    }

    private static class NameRowMapper implements RowMapper<Long> {
        @Override
        public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
            return rs.getLong("tenant_id");
        }
    }

    @Override
    protected String determineCurrentLookupKey() {
        String key = "tenant_" + TenantContext.getTenantId().toString();
        if (!isDataSourceKeyValid(key)) {
            addDataSource(key, createAndConfigureDataSource(key));
        }
        return key;
    }

    private boolean isDataSourceKeyValid(Object dataSourceKey) {
        return dataSourceMap.containsKey(dataSourceKey);
    }

    private DataSource createAndConfigureDataSource(String schema) {
        HikariDataSource ds = dataSourceProperties.initializeDataSourceBuilder()
                .type(HikariDataSource.class).build();

        ds.setUsername(username);
        ds.setPassword(password);
        ds.setJdbcUrl(urlPrefix + schema);

        ds.setPoolName(schema + TENANT_POOL_NAME_SUFFIX);

//        log.info("Configured datasource: {}", ds.getPoolName());
        return ds;
    }
}
