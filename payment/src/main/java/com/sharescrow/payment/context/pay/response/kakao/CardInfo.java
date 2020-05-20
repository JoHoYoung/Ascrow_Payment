package com.sharescrow.payment.context.pay.response.kakao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardInfo {
	private String interest_free_install; //N
	private String bin; //621640
	private String card_type; //체크
	private String card_mid; //123456789
	private String approved_id; //12345678
	private String install_month; //00
	private String purchase_corp; //비씨카드
	private String purchase_corp_code; //01
	private String issuer_corp; //수협카드
	private String issuer_corp_code; //13
	private String kakaopay_purchase_corp; //비씨카드
	private String kakaopay_purchase_corp_code; //104
	private String kakaopay_issuer_corp; //수협은행
	private String kakaopay_issuer_corp_code; //212
}
