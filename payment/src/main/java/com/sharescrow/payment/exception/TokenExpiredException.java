package com.sharescrow.payment.exception;

import com.sharescrow.payment.ErrorCode;

public class TokenExpiredException extends BusinessException {
	public TokenExpiredException(ErrorCode errorcode) {
		super(errorcode);
	}
}

