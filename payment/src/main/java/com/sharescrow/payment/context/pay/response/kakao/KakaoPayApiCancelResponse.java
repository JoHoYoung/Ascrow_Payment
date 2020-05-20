package com.sharescrow.payment.context.pay.response.kakao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KakaoPayApiCancelResponse {
	private String aid; //A5678901234567890123
	private String tid; //T1234567890123456789
	private String cid; //TC0ONETIME
	private String status; //CANCEL_PAYMENT
	private String partner_order_id; //partner_order_id
	private String partner_user_id; //partner_user_id
	private String payment_method_type; //MONEY
	private String item_name; //초코파이
	private int quantityprivate; //1
	private Amount amount;
	private Amount canceled_amount;
	private Amount cancel_available_amount;
	private Date created_at; //2016-11-15T21:18:22
	private Date approved_at; //2016-11-15T21:20:48
	private Date canceled_at;
}
