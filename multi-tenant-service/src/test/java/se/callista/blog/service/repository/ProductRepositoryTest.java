package se.callista.blog.service.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.database.rider.core.api.dataset.DataSet;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import se.callista.blog.service.annotation.SpringBootDbIntegrationTest;
import se.callista.blog.service.domain.entity.Product;
import se.callista.blog.service.multitenancy.util.TenantContext;
import se.callista.blog.service.persistence.PostgresqlTestContainer;
import se.callista.blog.service.util.DatabaseInitializer;

@Testcontainers
@SpringBootDbIntegrationTest
class ProductRepositoryTest {

    @Container
    private static final PostgresqlTestContainer POSTGRESQL_CONTAINER = PostgresqlTestContainer.getInstance();

    @Autowired
    private DatabaseInitializer databaseInitializer;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    @DataSet(value = {"shards.yml", "tenants.yml"})
    public void initialize() throws Exception {
        databaseInitializer.ensureInitialized();
    }

    @Test
    public void findByIdForTenant1() {

//        TenantContext.setTenantId("tenant1");
        TenantContext.setTenantId(1L);
        Optional<Product> product = productRepository.findById(1L);
        assertThat(product).isPresent();
        assertThat(product.get().getName()).isEqualTo("Product 1");
        TenantContext.clear();

    }

    @Test
    public void findByIdForTenant2() {

//        TenantContext.setTenantId("tenant2");
        TenantContext.setTenantId(2L);
        assertThat(productRepository.findById(1L)).isNotPresent();
        TenantContext.clear();

    }

    @Test
    public void findByIdForTenant3() {

//        TenantContext.setTenantId("tenant3");
        TenantContext.setTenantId(3L);
        Optional<Product> product = productRepository.findById(3L);
        assertThat(product).isPresent();
        assertThat(product.get().getName()).isEqualTo("Product 3");
        TenantContext.clear();

    }


}