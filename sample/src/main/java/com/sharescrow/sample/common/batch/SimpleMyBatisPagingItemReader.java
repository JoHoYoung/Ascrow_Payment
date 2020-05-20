package com.sharescrow.sample.common.batch;

import static org.springframework.util.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.springframework.batch.item.database.AbstractPagingItemReader;

import lombok.Setter;

public class SimpleMyBatisPagingItemReader<T> extends AbstractPagingItemReader<T> {
  @Setter
  private String queryId;
  @Setter
  private SqlSessionFactory sqlSessionFactory;
  private SqlSessionTemplate sqlSessionTemplate;
  @Setter
  private Map<String, Object> parameterValues;

  @Override
  public void afterPropertiesSet() {
  }

  @Override
  public void doReadPage() {
    if (Objects.isNull(this.sqlSessionTemplate)) {
      // Transaction 중 ExecutorType 변경으로 인한 에러 방지
      this.sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory, ExecutorType.SIMPLE);
    }
    Map<String, Object> parameters = new HashMap<>();
    if (parameterValues != null) {
      parameters.putAll(parameters);
    }
    parameters.put("_page", getPage());
    parameters.put("_pagesize", getPageSize());
    parameters.put("_skiprows", getPage() * getPageSize());
    if (results == null) {
      results = new CopyOnWriteArrayList<>();
    } else {
      results.clear();
    }
    results.addAll(sqlSessionTemplate.selectList(queryId, parameters));
  }

  @Override
  protected void doJumpToPage(int i) {

  }
}
