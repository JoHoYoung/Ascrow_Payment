package com.sharescrow.payment.context.product.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductValidRequest {
	private int userId;
	private int productOptionId;
	private int quantity;
	private int minShare;
}
