package se.callista.blog.service.multitenancy.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class TenantContext {

    private TenantContext() {}

//    private static final InheritableThreadLocal<Long> currentTenant = new InheritableThreadLocal<>();

    private static final ThreadLocal<Long> currentTenant = new ThreadLocal<>();

    public static void setTenantId(Long tenantId) {
        log.debug("Setting tenantId to " + tenantId);
        currentTenant.set(tenantId);
    }

    public static Long getTenantId() {
        Long res = currentTenant.get();
        return res;
    }

    public static void clear(){
        currentTenant.remove();
    }
}