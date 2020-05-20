package com.sharescrow.payment.exception;

import com.sharescrow.payment.ErrorCode;

public class OrderFailException extends BusinessException {
	public OrderFailException(ErrorCode errorCode) {
		super(errorCode);
	}
}
