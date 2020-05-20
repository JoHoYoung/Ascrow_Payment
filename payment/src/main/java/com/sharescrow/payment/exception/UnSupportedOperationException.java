package com.sharescrow.payment.exception;

import com.sharescrow.payment.ErrorCode;

public class UnSupportedOperationException extends BusinessException {
	public UnSupportedOperationException(ErrorCode errorCode) {
		super(errorCode);
	}
}
