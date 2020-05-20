package com.sharescrow.payment.exception;

import com.sharescrow.payment.ErrorCode;

public class InvalidOrderRequestException extends BusinessException {
	public InvalidOrderRequestException(ErrorCode errorcode) {
		super(errorcode);
	}
}
