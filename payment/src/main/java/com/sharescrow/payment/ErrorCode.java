package com.sharescrow.payment;

public enum ErrorCode {

	INVALID_REQUEST_PARAM(701, "A001", "Invalid Request Param"),
	INVALID_ORDER_DATA_FORMAT(702, "A002", "Invalid Order Data Format"),
	INVALID_PAYREQUEST_TYPE(703, "A003", "Invalid Type of Payment"),

	// JWT Token
	JWT_TOKEN_EXPIRED(801, "J001", "Token Expired"),
	INVALID_TOKEN(802, "J002", "Invalid Token"),
	EMPTY_TOKEN(803, "J003", "Empty Token"),
	DECODED_TOKEN_PARSE_ERROR(804, "J004", "Error at Parse decoded Token"),
	UNAUTHORIZED_ACCESS(805,"J005","Un Authorized Access"),
	// Product
	PRODUCT_INVALID(901, "P001", "User cannot buy product"),
	TRANASACTION_CANCEL_FAIL(902,"P002","Transaction cancel Fail"),
	UNSUPPORTED_OPERATION(1001, "B001", "Unsupported Operation"),
	// Date parsing
	DATE_PARSE_ERROR(1101, "C001", "Date Parse Error"),

	EMPTY_DATA_SET(1201, "D001", "Empty Data Set"),
	INTERNAL_SERVER_ERROR(1301, "D002", "Internal Server Error"),

	FAIL_PAYMENT_TRANSACTION(1401,"T001","Transaction Fails");

	private final String statusCode;
	private final String statusMsg;
	private int status;

	ErrorCode(final int status, final String code, final String message) {
		this.status = status;
		this.statusMsg = message;
		this.statusCode = code;
	}

	public int getStatus() {
		return this.status;
	}

	public String getStatusCode() {
		return this.statusCode;
	}

	public String getStatusMsg() {
		return this.statusMsg;
	}
}

