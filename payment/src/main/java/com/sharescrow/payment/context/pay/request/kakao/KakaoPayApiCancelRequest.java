package com.sharescrow.payment.context.pay.request.kakao;

import lombok.Data;

@Data
public class KakaoPayApiCancelRequest {
	private String cid; //TC0ONETIME' \
	private String tid; //T1234567890123456789' \
	private String cancel_amount; //2200' \
	private String cancel_tax_free_amount; //0' \
	private String cancel_vat_amount; //200' \
	private String cancel_available_amount; //4000'
}
