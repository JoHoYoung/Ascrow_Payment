package com.sharescrow.sample.sample;

import java.io.IOException;

import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableTransactionManagement
@MapperScan(
  basePackages = "com.sharescrow.sample.repository"
)
public class DataBaseConfig {
  @Autowired
  private ApplicationContext applicationContext;

  @Value("${db.password}")
  private String password;

  @Bean
  @ConfigurationProperties(prefix = "spring.datasource.hikari")
  public HikariConfig hikariConfig() {
    HikariConfig hikariConfig = new HikariConfig();
    hikariConfig.setPassword(password);
    return hikariConfig;
  }

  @Bean
  public DataSource dataSource() {
    DataSource dataSource = new HikariDataSource(hikariConfig());
    log.info("Loading Datasource : {}", dataSource);
    return dataSource;
  }

  @Bean(name = "transactionManager")
  public PlatformTransactionManager transactionManager(@Qualifier("dataSource") DataSource dataSource) {
    DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
    log.info("Loading TransactionManager : {}", transactionManager);
    return transactionManager;
  }

  @Bean
  SqlSessionFactoryBean sqlSessionFactory() throws IOException {
    SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
    sessionFactoryBean.setDataSource(dataSource());
    sessionFactoryBean.setTypeAliasesPackage("com.sharescrow.sample.model");
    sessionFactoryBean.setTypeHandlersPackage("com.sharescrow.sample.common.handler");
    sessionFactoryBean.setFailFast(true);
    sessionFactoryBean.setConfigLocation(applicationContext.getResource("classpath:/mybatis-config.xml"));
    sessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:/mapper/*Mapper.xml"));
    return sessionFactoryBean;
  }
}
