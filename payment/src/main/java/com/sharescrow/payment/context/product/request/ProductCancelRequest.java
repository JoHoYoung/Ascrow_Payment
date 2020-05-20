package com.sharescrow.payment.context.product.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductCancelRequest {
	private int userId;
	private int productId;
	private int productOptionId;
	private int quantity;
}