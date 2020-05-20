package com.sharescrow.payment.service.apiService.uri;

public enum QueueURI {

	ENQUEUE("/approve", "naver pay approve api");

	private String baseUri = "localhost:8080";
	private String endPoint;
	private String description;

	QueueURI(String endPoint, String description) {
		this.endPoint = endPoint;
		this.description = description;
	}

	QueueURI(String baseUri, String endPoint, String description) {
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
