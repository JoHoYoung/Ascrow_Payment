package com.sharescrow.payment.service.apiService.uri;

public enum OpenBankURI {

	APPROVE("/approve", "naver pay approve api"),
	CANCEL("/cancel", "naver pay cancel api");

	private String baseUri = "localhost:8080";
	private String endPoint;
	private String description;

	OpenBankURI(String endPoint, String description) {
		this.endPoint = endPoint;
		this.description = description;
	}

	OpenBankURI(String baseUri, String endPoint, String description) {
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
