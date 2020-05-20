package com.sharescrow.payment.context.product.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductValidResponse {
	private int statusCode;
	private String statusMsg;
	private String productName;
	private int price;
}
