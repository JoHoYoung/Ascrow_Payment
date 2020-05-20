package com.sharescrow.payment.context.pay.response.kakao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KakaoPayApiApproveMoneyResponse {
	private String aid; //A5678901234567890123
	private String tid; //T1234567890123456789
	private String cid; //TC0ONETIME
	private String partner_order_id; //partner_order_id
	private String partner_user_id; //partner_user_id
	private String payment_method_type; //MONEY
	private String item_name; //초코파이
	private String quantityprivate;
	private Amount amount;
	private String created_at; //2016-11-15T21:18:22
	private String approved_at; //2016-11-15T21:20:47
}
