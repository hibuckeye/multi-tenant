package se.callista.blog.service.multitenancy.config.tenant;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;

@RequiredArgsConstructor
@Configuration
@EnableJpaRepositories(
        basePackages = { "${multitenancy.tenant.repository.packages}" },
        entityManagerFactoryRef = "tenantEntityManager",
        transactionManagerRef = "tenantTransactionManager")
public class TenantConfiguration {

    @Value("${multitenancy.tenant.entityManager.packages}")
    private String entityPackages;

    @Bean(name = "tenantEntityManager")
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(@Qualifier("tenantDataSource") DataSource dataSource,
                                                                           EntityManagerFactoryBuilder builder) {
        return builder.dataSource(dataSource).packages(entityPackages).build();
    }

    @Primary
    @Bean(name = "tenantTransactionManager")
    public JpaTransactionManager transactionManager(
            @Autowired @Qualifier("tenantEntityManager") LocalContainerEntityManagerFactoryBean entityManagerFactoryBean) {
        return new JpaTransactionManager(entityManagerFactoryBean.getObject());
    }

}
