package com.sharescrow.payment.exception;

import com.sharescrow.payment.ErrorCode;

public class NotFoundException extends BusinessException {
	public NotFoundException(ErrorCode errorCode) {
		super(errorCode);
	}
}
