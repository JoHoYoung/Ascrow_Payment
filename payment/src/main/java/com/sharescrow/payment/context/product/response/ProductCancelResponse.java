package com.sharescrow.payment.context.product.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCancelResponse {
	private int statusCode;
	private String statusMsg;
}
