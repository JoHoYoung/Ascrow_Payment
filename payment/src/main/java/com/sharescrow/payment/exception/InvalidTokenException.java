package com.sharescrow.payment.exception;

import com.sharescrow.payment.ErrorCode;

public class InvalidTokenException extends BusinessException {
	public InvalidTokenException(ErrorCode errorcode) {
		super(errorcode);
	}
}
