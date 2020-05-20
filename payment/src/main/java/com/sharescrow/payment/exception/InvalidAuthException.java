package com.sharescrow.payment.exception;

import com.sharescrow.payment.ErrorCode;

public class InvalidAuthException extends BusinessException {
	public InvalidAuthException(ErrorCode errorCode) {
		super(errorCode);
	}
}
