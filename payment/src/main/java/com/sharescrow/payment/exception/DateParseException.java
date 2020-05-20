package com.sharescrow.payment.exception;

import com.sharescrow.payment.ErrorCode;

public class DateParseException extends BusinessException {
	public DateParseException(ErrorCode errorCode) {
		super(errorCode);
	}
}
