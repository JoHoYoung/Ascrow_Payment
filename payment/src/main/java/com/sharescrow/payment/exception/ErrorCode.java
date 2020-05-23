package com.sharescrow.payment.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

public enum ErrorCode {

	INVALID_REQUEST_PARAM(701, HttpStatus.BAD_REQUEST, "Invalid Request Param"),
	INVALID_ORDER_DATA_FORMAT(702, HttpStatus.BAD_REQUEST, "Invalid Order Data Format"),
	INVALID_PAYREQUEST_TYPE(703, HttpStatus.BAD_REQUEST, "Invalid Type of Payment"),

	// JWT Token
	JWT_TOKEN_EXPIRED(801, HttpStatus.UNAUTHORIZED, "Token Expired"),
	INVALID_TOKEN(802, HttpStatus.UNAUTHORIZED, "Invalid Token"),
	EMPTY_TOKEN(803, HttpStatus.BAD_REQUEST, "Empty Token"),
	DECODED_TOKEN_PARSE_ERROR(804, HttpStatus.UNAUTHORIZED, "Error at Parse decoded Token"),
	UNAUTHORIZED_ACCESS(805,HttpStatus.UNAUTHORIZED,"Un Authorized Access"),
	// Product
	PRODUCT_INVALID(901, HttpStatus.BAD_REQUEST, "User cannot buy product"),
	TRANASACTION_CANCEL_FAIL(902,HttpStatus.INTERNAL_SERVER_ERROR,"Transaction cancel Fail"),
	UNSUPPORTED_OPERATION(1001, HttpStatus.BAD_REQUEST, "Unsupported Operation"),
	// Date parsing
	DATE_PARSE_ERROR(1101, HttpStatus.BAD_REQUEST, "Date Parse Error"),

	EMPTY_DATA_SET(1201, HttpStatus.NOT_FOUND, "Empty Data Set"),
	INTERNAL_SERVER_ERROR(1301, HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"),

	FAIL_PAYMENT_TRANSACTION(1401,HttpStatus.INTERNAL_SERVER_ERROR,"Transaction Fails");

	@Getter
	private final HttpStatus httpStatusCode;
	@Getter
	private final String statusMsg;
	@Getter
	private int status;

	ErrorCode(final int status, HttpStatus httpStatusCode, final String message) {
		this.status = status;
		this.statusMsg = message;
		this.httpStatusCode = httpStatusCode;
	}

}

