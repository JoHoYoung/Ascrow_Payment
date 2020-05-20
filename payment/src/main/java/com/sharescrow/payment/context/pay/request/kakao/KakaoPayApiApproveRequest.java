package com.sharescrow.payment.context.pay.request.kakao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KakaoPayApiApproveRequest {
	private String cid; //TC0ONETIME' \
	private String tid; //T1234567890123456789' \
	private String partner_order_id; //partner_order_id' \
	private String partner_user_id; //partner_user_id' \
	private String pg_token; //xxxxxxxxxxxxxxxxxxxx'
}
