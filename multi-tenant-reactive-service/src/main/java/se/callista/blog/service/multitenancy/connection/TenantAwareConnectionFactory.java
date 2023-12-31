package se.callista.blog.service.multitenancy.connection;

import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryMetadata;
import io.r2dbc.spi.Statement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import se.callista.blog.service.multitenancy.util.TenantContext;

@RequiredArgsConstructor
@Slf4j
public class TenantAwareConnectionFactory implements ConnectionFactory {

    // Copy of io.r2dbc.postgresql.PostgresqlConnectionFactoryMetadata, which is not public
    static final class PostgresqlConnectionFactoryMetadata implements ConnectionFactoryMetadata {

        static final PostgresqlConnectionFactoryMetadata INSTANCE = new PostgresqlConnectionFactoryMetadata();

        public static final String NAME = "PostgreSQL";

        private PostgresqlConnectionFactoryMetadata() {
        }

        @Override
        public String getName() {
            return NAME;
        }
    }

    private final ConnectionFactory connectionFactory;

    @Override
    public Publisher<? extends Connection> create() {
        return TenantContext.getTenantId()
                .switchIfEmpty(Mono.defer(() ->
                    Mono.error(new RuntimeException(String.format("ContextView does not contain the Lookup Key '%s'", TenantContext.TENANT_KEY)))))
                .flatMapMany(this::createConnection);
    }

    private Mono<Connection> createConnection(Object tenant) {
        return Mono.from(connectionFactory.create())
                .flatMap(connection -> setTenant(connection, (String) tenant));
    }

    private Mono<Connection> setTenant(Connection connection, String tenantId) {
        final Statement statement = connection.createStatement("SET app.tenant_id TO '" + tenantId + "'");
        return Mono.from(statement.execute())
                .doOnSuccess(e -> log.debug("set tenant to {}", tenantId))
                .map(e -> connection);
    }

    @Override
    public ConnectionFactoryMetadata getMetadata() {
        return PostgresqlConnectionFactoryMetadata.INSTANCE;
    }
}