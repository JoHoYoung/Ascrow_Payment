package com.sharescrow.payment.context.pay.response.kakao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KakaoPayApiReadyResponse {
	private String tid; // T1234567890123456789
	private String next_redirect_app_url; //https://mockup-pg-web.kakao.com/v1/xxxxxxxxxx/aInfo
	private String next_redirect_mobile_url; // https://mockup-pg-web.kakao.com/v1/xxxxxxxxxx/mInfo
	private String next_redirect_pc_url; //  https://mockup-pg-web.kakao.com/v1/xxxxxxxxxx/info
	private String android_app_scheme; //  kakaotalk://kakaopay/pg?url=https://mockup-pg-web.kakao.com/v1/xxxxxxxxxx/order
	private String ios_app_scheme; //   kakaotalk://kakaopay/pg?url=https://mockup-pg-web.kakao.com/v1/xxxxxxxxxx/order
	private String created_at; //  2016-11-15T21:18:22
}
