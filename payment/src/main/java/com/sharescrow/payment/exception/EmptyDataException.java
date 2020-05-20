package com.sharescrow.payment.exception;

import com.sharescrow.payment.ErrorCode;
import com.sharescrow.payment.exception.BusinessException;

public class EmptyDataException extends BusinessException {
	public EmptyDataException(ErrorCode errorCode) {
		super(errorCode);
	}
}
