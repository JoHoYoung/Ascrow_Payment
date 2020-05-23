package com.sharescrow.payment.context.queue.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnqueueRequest {
	private int orderId;
	private int productId;
	private int userId;
	private int minShare;
	private int maxShare;
}
