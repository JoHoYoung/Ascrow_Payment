package com.sharescrow.sample.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.mybatis.spring.batch.builder.MyBatisPagingItemReaderBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.sharescrow.sample.common.batch.SimpleMyBatisPagingItemReader;
import com.sharescrow.sample.model.Product;
import com.sharescrow.sample.model.ProductHistory;
import com.sharescrow.sample.repository.ProductRepository;

@Configuration("productHistoryJobConf")
public class ProductHistoryJobConf {
  private static final String JOB_NAME = "productHistoryJob";
  private static final String STEP_NAME = "productHistoryStep";
  private static final int PAGE_SIZE = 1000;
  private static final int CHUNK_SIZE = 100;

  @Autowired
  private JobBuilderFactory jobBuilderFactory;

  @Autowired
  private StepBuilderFactory stepBuilderFactory;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private SqlSessionFactory sqlSessionFactory;

  @Bean("productHistoryJob")
  public Job getProductHistoryJob() {
    return jobBuilderFactory.get(JOB_NAME)
      .start(productHistoryStep())
      .build();
  }

  @Bean("productHistoryStep")
  public Step productHistoryStep() {
    return stepBuilderFactory.get(STEP_NAME)
      .<Product, ProductHistory>chunk(CHUNK_SIZE)
      .reader(productHistoryReader())
      .processor(productHistoryProcessor())
      .writer(productHistoryWriter())
      .build();
  }

  @Bean
  @StepScope
  public ItemReader<Product> productHistoryReader(
  ) {
    SimpleMyBatisPagingItemReader reader = new SimpleMyBatisPagingItemReader();
    reader.setQueryId("com.sharescrow.sample.repository.ProductRepository.selectProductsWithoutHistory");
    reader.setPageSize(PAGE_SIZE);
    reader.setSqlSessionFactory(sqlSessionFactory);
    return reader;
  }

  @Bean
  @StepScope
  public ItemProcessor<Product, ProductHistory> productHistoryProcessor() {
    return ProductHistory::fromProduct;
  }

  @Bean
  @StepScope
  public ItemWriter<ProductHistory> productHistoryWriter() {
    return (productHistories) -> productRepository.insertProductHistories((List<ProductHistory>)productHistories);

  }

}
