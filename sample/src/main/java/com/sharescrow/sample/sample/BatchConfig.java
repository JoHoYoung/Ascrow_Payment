package com.sharescrow.sample.sample;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableBatchProcessing
@EnableTransactionManagement
@MapperScan(basePackages = "com.sharescrow.sample.repository")
@ComponentScan(basePackages = {"com.sharescrow.sample"})
public class BatchConfig {
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

  @Bean
  public HttpMessageConverter<String> responseBodyConverter() {
    return new StringHttpMessageConverter(Charset.forName("UTF-8"));
  }

  @Bean
  BatchConfigurer configurer(@Qualifier("dataSource") DataSource dataSource) {
    return new DefaultBatchConfigurer(dataSource);
  }

  @Bean
  public StepBuilderFactory stepBuilderFactory(
    JobRepository jobRepository,
    @Qualifier("transactionManager") PlatformTransactionManager transactionManager
  ) {
    return new StepBuilderFactory(jobRepository, transactionManager);
  }

  @Bean(name = "syncJobLauncher")
  public JobLauncher syncJobLauncher(JobRepository jobRepository) {
    SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
    jobLauncher.setJobRepository(jobRepository);

    SyncTaskExecutor taskExecutor = new SyncTaskExecutor();
    jobLauncher.setTaskExecutor(taskExecutor);

    return jobLauncher;
  }
}
