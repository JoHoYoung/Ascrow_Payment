package com.sharescrow.payment.context.pay.response.naver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NaverPayApiCancelResponse {
	private String code;
	private CancelBody body;
}
