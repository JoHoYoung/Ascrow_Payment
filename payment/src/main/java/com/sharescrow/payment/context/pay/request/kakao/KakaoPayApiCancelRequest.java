package com.sharescrow.payment.context.pay.request.kakao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KakaoPayApiCancelRequest {
	private String cid; //TC0ONETIME' \
	private String tid; //T1234567890123456789' \
	private Integer cancel_amount; //2200' \
	private Integer cancel_tax_free_amount; //0' \
	private Integer cancel_vat_amount; //200' \
	private Integer cancel_available_amount; //4000'
}
