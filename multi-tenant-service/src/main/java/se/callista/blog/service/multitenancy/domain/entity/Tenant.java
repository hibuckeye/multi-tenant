package se.callista.blog.service.multitenancy.domain.entity;

import java.util.Objects;
import javax.persistence.*;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Size(max = 30)
    @Column(name = "tenant_id")
    private Long tenantId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    private Shard shard;

    @Column(name = "schema")
    private String schema;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tenant that = (Tenant) o;
        return getTenantId() != null && getTenantId().equals(that.getTenantId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTenantId());
    }

}