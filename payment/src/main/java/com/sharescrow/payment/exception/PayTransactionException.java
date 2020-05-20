package com.sharescrow.payment.exception;

import com.sharescrow.payment.ErrorCode;

public class PayTransactionException extends BusinessException {
	public PayTransactionException(ErrorCode errorCode) {
		super(errorCode);
	}
}