package com.sharescrow.payment.service.apiService.uri;

public enum NaverPayURI {

	APPROVE("/approve", "naver pay approve api"),
	CANCEL("/cancel", "naver pay cancel api"),
	CALL_BACK("localhost:8080","/api/v1/order/transaction/done/NAVER_PAY/","when naver pay done, cancel and fail call this uri");
	private String baseUri = "localhost:8080";
	private String endPoint;
	private String description;

	NaverPayURI(String endPoint, String description) {
		this.endPoint = endPoint;
		this.description = description;
	}

	NaverPayURI(String baseUri, String endPoint, String description) {
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
