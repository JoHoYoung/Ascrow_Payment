package com.sharescrow.sample.service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sharescrow.sample.common.exception.ApplicationError;
import com.sharescrow.sample.common.exception.ApplicationException;
import com.sharescrow.sample.model.Product;
import com.sharescrow.sample.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProductService {
  @Autowired
  private ProductRepository productRepository;

  public Product getProductByProductNo(Long productNo) {
    Product product =  productRepository.selectProduct(productNo);
    if (Objects.isNull(product)) {
      throw new ApplicationException(ApplicationError.NOT_FOUND);
    }
    return product;
  }

  public List<Product> getProducts(int offset, int page) {
    return productRepository.selectProducts(offset, page);
  }

  public Product createProduct(Product product) {
    productRepository.insertProduct(product);
    return product;
  }

  public Product editProduct(long productNo, Product product) {
    if (Objects.nonNull(product.getProductNo()) &&
      product.getProductNo() > 0L &&
      productNo != product.getProductNo()) {
      throw new ApplicationException(ApplicationError.BAD_REQUEST);
    }
    productRepository.updateProduct(productNo, product);
    return productRepository.selectProduct(productNo);
  }
}
