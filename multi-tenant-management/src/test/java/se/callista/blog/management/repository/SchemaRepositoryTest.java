//package se.callista.blog.management.repository;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//import com.github.database.rider.core.api.dataset.DataSet;
//import java.util.Optional;
//import liquibase.integration.spring.SpringLiquibase;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//import se.callista.blog.management.annotation.SpringBootDbIntegrationTest;
//import se.callista.blog.management.domain.entity.Shard;
//import se.callista.blog.management.persistence.PostgresqlTestContainer;
//
//@Testcontainers
//@SpringBootDbIntegrationTest
//class SchemaRepositoryTest {
//
//    @Container
//    private static final PostgresqlTestContainer POSTGRESQL_CONTAINER = PostgresqlTestContainer.getInstance();
//
//    @Autowired // Autowired to force eager loading
//    private SpringLiquibase springLiquibase;
//
//    @Autowired
//    private SchemaRepository schemaRepository;
//
//    @Test
//    @DataSet(value = {"repository/shards.yml", "repository/tenants.yml"})
//    public void findById() throws Exception {
//
//        Optional<Shard> shard = schemaRepository.findById(1);
//        assertThat(shard).isPresent();
//        assertThat(shard.get().getDb()).endsWith("shard_1");
//
//    }
//
//}