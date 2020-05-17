package com.sharescrow.sample.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Product {
  private Long productNo;
  private String productId;
  private String productName;
  private BigDecimal price;
  private LocalDateTime createYmdt;
  private LocalDateTime updateYmdt;
}
