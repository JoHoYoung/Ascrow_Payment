package com.sharescrow.payment.context.pay.response.naver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApproveBody {
	private String paymentId;
	private ApproveDetail detail;
}
