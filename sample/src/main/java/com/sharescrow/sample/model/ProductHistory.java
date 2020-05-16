package com.sharescrow.sample.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ProductHistory {
  private Long productNo;
  private String productId;
  private String productName;
  private BigDecimal price;
  private LocalDateTime createYmdt;

  public static ProductHistory fromProduct(Product product) {
    ProductHistory productHistory = new ProductHistory();
    productHistory.setProductNo(product.getProductNo());
    productHistory.setProductId(product.getProductId());
    productHistory.setProductName(product.getProductName());
    productHistory.setPrice(product.getPrice());
    return productHistory;
  }
}
