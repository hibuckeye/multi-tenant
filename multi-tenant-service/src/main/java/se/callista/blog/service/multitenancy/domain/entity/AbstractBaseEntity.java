package se.callista.blog.service.multitenancy.domain.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import se.callista.blog.service.multitenancy.listener.TenantListener;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(TenantListener.class)
public abstract class AbstractBaseEntity implements TenantAware, Serializable {
    private static final long serialVersionUID = 1L;

//    @Size(max = 30)
    @Column(name = "tenant_id")
    private Long tenantId;

    public AbstractBaseEntity(Long tenantId) {
        this.tenantId = tenantId;
    }

}
