package se.callista.blog.management.domain.entity;

import java.util.Objects;
import javax.persistence.*;

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
//    @Size(max = 30)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tenant_id")
    private Long tenantId;

    @Column(name = "name")
    private String name;

//    @ManyToOne(fetch = FetchType.LAZY)
//    private Shard shard;

    @Column(name = "db")
    private String db;

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