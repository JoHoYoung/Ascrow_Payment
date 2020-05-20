package com.sharescrow.payment.exception;

import com.sharescrow.payment.ErrorCode;

public class InvalidPayTypeException extends BusinessException {
	public InvalidPayTypeException(ErrorCode errorCode) {
		super(errorCode);

	}
}