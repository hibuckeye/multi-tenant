package se.callista.blog.service.multitenancy.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import se.callista.blog.service.multitenancy.util.TenantContext;

/**
 * Tenant-Aware Datasource that decorates Connections with
 * current tenant information.
 */
public class MultiTenantDataSource extends AbstractRoutingDataSource {

    @Override
    protected String determineCurrentLookupKey() {
        String res = TenantContext.getTenantId().toString();
        return "tenant_" + res;
    }

}
