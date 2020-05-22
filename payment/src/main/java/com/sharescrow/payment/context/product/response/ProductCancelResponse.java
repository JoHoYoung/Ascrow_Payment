package com.sharescrow.payment.context.product.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductCancelResponse {
	private Integer statusCode;
	private String statusMsg;
}
