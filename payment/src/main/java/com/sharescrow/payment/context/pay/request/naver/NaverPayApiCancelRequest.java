package com.sharescrow.payment.context.pay.request.naver;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NaverPayApiCancelRequest {
	private String paymentId; //{결제 완료 시 전달된 paymentId} \
	private int cancelAmount; //{취소금액} \
	private String cancelReason; //testCancel \
	private String cancelRequester; //2
}
