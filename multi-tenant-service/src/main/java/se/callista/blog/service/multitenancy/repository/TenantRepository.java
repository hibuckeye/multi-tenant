package se.callista.blog.service.multitenancy.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import se.callista.blog.service.multitenancy.domain.entity.Tenant;

public interface TenantRepository extends CrudRepository<Tenant, String> {

    @Query("SELECT t FROM Tenant t WHERE t.tenantId = :tenantId")
    Optional<Tenant> findByTenantId(Long tenantId);

}