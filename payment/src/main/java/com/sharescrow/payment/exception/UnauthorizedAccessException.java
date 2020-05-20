package com.sharescrow.payment.exception;

import com.sharescrow.payment.ErrorCode;

public class UnauthorizedAccessException extends BusinessException {
	public UnauthorizedAccessException(ErrorCode errorCode) {
		super(errorCode);
	}
}
