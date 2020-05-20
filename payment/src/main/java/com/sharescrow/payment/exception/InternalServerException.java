package com.sharescrow.payment.exception;

import com.sharescrow.payment.ErrorCode;

public class InternalServerException extends BusinessException {
	public InternalServerException(ErrorCode errorCode) {
		super(errorCode);
	}
}
