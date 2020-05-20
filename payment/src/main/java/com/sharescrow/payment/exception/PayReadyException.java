package com.sharescrow.payment.exception;

import com.sharescrow.payment.ErrorCode;

public class PayReadyException extends BusinessException {
	public PayReadyException(ErrorCode errorCode) {
		super(errorCode);
	}
}
