package com.sharescrow.payment.context.product.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductValidResponse {
	private String productName;
	private Integer transAmount;
	private Integer quantity;
	private Integer maxShareInteger;
	private Integer pyterm;
}
