package com.sharescrow.sample.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.sharescrow.sample.model.Product;

@Repository
public interface ProductRepository {
  List<Product> selectProducts(
    @Param("offset") int offset
    , @Param("pageSize") int pageSize
  );

  Product selectProduct(
    @Param("productNo") Long productNo
  );

  void insertProduct(
    Product product
  );

  void updateProduct(
    @Param("productNo") long productNo
    , @Param("product") Product product
  );
}
