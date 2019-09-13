package com.webservicemaster.iirs.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
 
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
 
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
		entityManagerFactoryRef = "transactionEntityManager", 
		transactionManagerRef = "transactionTransactionManager", 
		basePackages = "com.webservicemaster.iirs.dao.transaction"
)
public class TransactionConfiguration {

	@Bean
	@ConfigurationProperties( prefix = "spring.ds_trans" )
	public DataSource transactionDataSource(){
		return DataSourceBuilder.create().build();
	}
	
	@Bean(name = "transactionEntityManager")
	@PersistenceContext(unitName="transactionPU")
	public LocalContainerEntityManagerFactoryBean transactionEntityManagerFactory(EntityManagerFactoryBuilder builder) {
		return builder
					.dataSource(transactionDataSource())
					.packages("com.webservicemaster.iirs.domain.transaction")
					.persistenceUnit("transactionPU")
					.build();
	}
	
	@Bean(name = "transactionTransactionManager")
	public PlatformTransactionManager transactionTransactionManager(@Qualifier("transactionEntityManager") EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}
	
	
	/*
	private Map<String, Object> hibernateProperties() {
		 
		Resource resource = new ClassPathResource("hibernate.properties");
		
		try {
			Properties properties = PropertiesLoaderUtils.loadProperties(resource);
			return properties.entrySet().stream().collect(Collectors.toMap( e->e.getKey().toString(), e->e.getValue()) );
		} catch (IOException e) {
			return new HashMap<String, Object>();
		}
	}*/
	
}
