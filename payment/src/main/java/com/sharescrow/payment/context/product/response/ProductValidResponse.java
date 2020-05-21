package com.sharescrow.payment.context.product.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductValidResponse {
	private String productName;
	private int transAmount;
	private int quantity;
	private int maxShareint;
	private int pyterm;
}
