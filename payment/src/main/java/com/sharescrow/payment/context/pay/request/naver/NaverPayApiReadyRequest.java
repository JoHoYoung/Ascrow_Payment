package com.sharescrow.payment.context.pay.request.naver;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class NaverPayApiReadyRequest {
	private String returnUrl;
}
