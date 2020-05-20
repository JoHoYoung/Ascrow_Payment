package com.sharescrow.payment.context.pay.response.naver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NaverPayApiApproveResponse {
	private String code;
	private String message;
	private ApproveBody body;
}
