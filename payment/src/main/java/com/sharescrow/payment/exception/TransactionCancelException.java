package com.sharescrow.payment.exception;

import com.sharescrow.payment.ErrorCode;

public class TransactionCancelException extends BusinessException {
	public TransactionCancelException(ErrorCode errorCode) {
		super(errorCode);
	}
}
