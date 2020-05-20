package com.sharescrow.payment.exception;

import com.sharescrow.payment.ErrorCode;

public class DecodedTokenParseException extends BusinessException {
	public DecodedTokenParseException(ErrorCode errorCode) {
		super(errorCode);
	}
}
