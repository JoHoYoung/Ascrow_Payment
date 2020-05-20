package com.sharescrow.payment.exception;

import com.sharescrow.payment.ErrorCode;

public class TransactionFailException extends BusinessException {
	public TransactionFailException(ErrorCode errorCode) {
		super(errorCode);
	}
}
