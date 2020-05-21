package com.sharescrow.payment.exception;

import com.sharescrow.payment.ErrorCode;

public class TransactionCancelFailException extends BusinessException {
	public TransactionCancelFailException(ErrorCode errorCode){
		super(errorCode);
	}
}
