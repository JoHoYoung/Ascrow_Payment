package com.sharescrow.payment.context.pay.response.kakao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KakaoPayApiApproveCardResponse {
	private String cid; // TC0ONETIME
	private String aid; // A5678901234567890123
	private String tid; // T1234567890123456789
	private String partner_user_id; // partner_user_id
	private String partner_order_id; // partner_order_id
	private String payment_method_type; // CARD
	private String item_name; // 카페아메리카노
	private String quantity; // 1,
	private Amount amount;
	private CardInfo cardInfo;
	private String created_at; // 2019-05-21T11:18:24
	private String approved_at; // 2019-05-21T11:18:32
}
