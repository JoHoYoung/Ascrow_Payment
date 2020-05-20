package com.sharescrow.payment.context.pay.request.naver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NaverPayApiApproveRequest {
	private String paymentId;
}
