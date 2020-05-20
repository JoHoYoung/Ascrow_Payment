package com.sharescrow.payment.service.apiService.uri;

public enum ProductURI {

	VALID("/approve", "whether can buy or not"),
	CANCEL("/api/v1/order/test", "cancel dedicated order");

	private String baseUri = "localhost:8080";
	private String endPoint;
	private String description;

	ProductURI(String endPoint, String description) {
		this.endPoint = endPoint;
		this.description = description;
	}

	ProductURI(String baseUri, String endPoint, String description) {
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
