package se.callista.blog.service.multitenancy.domain.entity;

public interface TenantAware {

    Long getTenantId();

    void setTenantId(Long tenantId);
}
