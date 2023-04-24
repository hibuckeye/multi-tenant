package se.callista.blog.management.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.sql.DataSource;
import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringLiquibase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.io.ResourceLoader;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.StatementCallback;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import se.callista.blog.management.domain.entity.Shard;

@RequiredArgsConstructor
@Slf4j
@Service
@EnableConfigurationProperties(LiquibaseProperties.class)
public class SchemaInitializerImpl implements SchemaInitializer {

    private final JdbcTemplate jdbcTemplate;
    @Qualifier("shardLiquibaseProperties")
    private final LiquibaseProperties liquibaseProperties;
    private final ResourceLoader resourceLoader;

    @Value("${multitenancy.shard.datasource.url-prefix}")
    private String urlPrefix;
    @Value("${multitenancy.master.datasource.username}")
    private String username;
    @Value("${multitenancy.master.datasource.password}")
    private String password;

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void initializeDBSchema(Shard shard) {
        try {
            createDatabase(shard.getDb());
            log.info("Created new shard {}", shard.getDb());
        } catch (DataAccessException e) {
            throw new SchemaCreationException("Error when creating db: " + shard.getDb(), e);
        }
        try (Connection connection =
                        DriverManager.getConnection(urlPrefix + shard.getDb(), username, password)) {
            DataSource shardDataSource = new SingleConnectionDataSource(connection, false);
            runLiquibase(shardDataSource);
            log.info("Initialized shard {}", shard.getDb());
        } catch (SQLException | LiquibaseException e) {
            throw new SchemaCreationException("Error when populating db: ", e);
        }
    }

    private void createDatabase(String db) {
        jdbcTemplate.execute(
                        (StatementCallback<Boolean>) stmt -> stmt.execute("CREATE DATABASE " + db));
        jdbcTemplate.execute((StatementCallback<Boolean>) stmt -> stmt
                        .execute("GRANT ALL PRIVILEGES ON DATABASE " + db + " TO " + username));
    }

    private void runLiquibase(DataSource dataSource) throws LiquibaseException {
        SpringLiquibase liquibase = getSpringLiquibase(dataSource);
        liquibase.afterPropertiesSet();
    }

    protected SpringLiquibase getSpringLiquibase(DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setResourceLoader(resourceLoader);
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog(liquibaseProperties.getChangeLog());
        liquibase.setContexts(liquibaseProperties.getContexts());
        liquibase.setDefaultSchema(liquibaseProperties.getDefaultSchema());
        liquibase.setLiquibaseSchema(liquibaseProperties.getLiquibaseSchema());
        liquibase.setLiquibaseTablespace(liquibaseProperties.getLiquibaseTablespace());
        liquibase.setDatabaseChangeLogTable(liquibaseProperties.getDatabaseChangeLogTable());
        liquibase.setDatabaseChangeLogLockTable(
                        liquibaseProperties.getDatabaseChangeLogLockTable());
        liquibase.setDropFirst(liquibaseProperties.isDropFirst());
        liquibase.setShouldRun(liquibaseProperties.isEnabled());
        liquibase.setLabels(liquibaseProperties.getLabels());
        liquibase.setChangeLogParameters(liquibaseProperties.getParameters());
        liquibase.setRollbackFile(liquibaseProperties.getRollbackFile());
        liquibase.setTestRollbackOnUpdate(liquibaseProperties.isTestRollbackOnUpdate());
        return liquibase;
    }

}
