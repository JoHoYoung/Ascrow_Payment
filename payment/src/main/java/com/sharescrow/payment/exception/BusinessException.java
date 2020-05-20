package com.sharescrow.payment.exception;

import com.sharescrow.payment.ErrorCode;
import com.sharescrow.payment.util.DateUtil;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class BusinessException extends RuntimeException {
	private ErrorCode errorCode;
	private StringBuilder stringBuilder = new StringBuilder();

	@Autowired
	DateUtil dateUtil;

	public BusinessException(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	public ErrorCode getErrorCode() {
		return this.errorCode;
	}

	public int getStatus() {
		return this.errorCode.getStatus();
	}

	public String getStatusCode() {
		return this.errorCode.getStatusCode();
	}

	public String getStatusMsg() {
		return this.errorCode.getStatusMsg();
	}

	public String toString() {
		this.stringBuilder.append(this.getStatus());
		this.stringBuilder.append(" | ");
		this.stringBuilder.append(this.getStatusCode());
		this.stringBuilder.append(" | ");
		this.stringBuilder.append(this.getStatusMsg());
		return this.stringBuilder.toString();
	}
}
