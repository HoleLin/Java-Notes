package com.holelin.mysql.jpa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "jpaEntityManagerFactory",
        transactionManagerRef = "jpaTransactionManager",
        basePackages = {"com.holelin.mysql.jpa.dao"}
)
public class JpaConfig {


    @Autowired
    private JpaProperties jpaProperties;

    @Bean(name = "jpaEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("dataSource") DataSource dataSource) {
        return builder.dataSource(dataSource).
                properties(jpaProperties.getProperties()).
                packages("com.holelin.mysql.entity").
                build();
    }

    @Bean
    public EntityManagerFactoryBuilder entityManagerFactoryBuilder() {
        return new EntityManagerFactoryBuilder(new HibernateJpaVendorAdapter(), new HashMap<>(), null);
    }

    @Bean(name = "jpaEntityManager")
    public EntityManager entityManager(EntityManagerFactoryBuilder builder,
                                       @Qualifier("dataSource") DataSource dataSource) {
        final EntityManagerFactory managerFactory = entityManagerFactory(builder, dataSource).getObject();
        return managerFactory.createEntityManager();
    }

    @Bean(name = "jpaTransactionManager")
    public PlatformTransactionManager transactionManager(
            EntityManagerFactoryBuilder builder,
            @Qualifier("dataSource") DataSource dataSource) {
        final EntityManagerFactory managerFactory = entityManagerFactory(builder, dataSource).getObject();
        return new JpaTransactionManager(managerFactory);

    }
}
