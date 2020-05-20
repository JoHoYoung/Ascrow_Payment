package com.sharescrow.payment.service.apiService.uri;

public enum KakaoPayURI {

	READY("/ready", "kakao pay ready api"),
	APPROVE("/approve", "kakao pay approve api"),
	CANCEL("/cancel", "kakao pay cancel api"),
	DONE_CALLBACK("localhost:8080","/api/v1/order/transaction/done/KAKAO_PAY/","when kakao pay done callback to this uri"),
	CANCEL_CALLBACK("localhost:8080","/api/v1/transaction/kakaoPay/cancel/","when kakao pay canceled callback to this uri"),
	FAIL_CALLBACK("localhost:8080","/api/v1/transaction/kakaoPay/fail/","when kakao pay fails callback to this uri");

	private String baseUri = "localhost:8080";
	private String endPoint;
	private String description;

	KakaoPayURI(String endPoint, String description) {
		this.endPoint = endPoint;
		this.description = description;
	}

	KakaoPayURI(String baseUri, String endPoint, String description) {
		this.baseUri = baseUri;
		this.endPoint = endPoint;
		this.description = description;
	}

	public String getBaseUri() {
		return this.baseUri;
	}

	public String getEndPoint() {
		return this.endPoint;
	}

	public String getDescription() {
		return this.description;
	}

}
