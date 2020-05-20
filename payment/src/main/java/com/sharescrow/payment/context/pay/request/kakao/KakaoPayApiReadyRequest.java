package com.sharescrow.payment.context.pay.request.kakao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KakaoPayApiReadyRequest {
	private String cid; //TC0ONETIME' \
	private String partner_order_id; //partner_order_id' \
	private String partner_user_id; //partner_user_id'
	private String item_name; //초코파이' \
	private int quantity; //1' \
	private int total_amount; //2200' \
	private int vat_amount; //200' \
	private int tax_free_amount; //0' \
	private String approval_url; //https://developers.kakao.com/success' \
	private String fail_url; //https://developers.kakao.com/fail' \
	private String cancel_url; //https://developers.kakao.com/cancel'
}
