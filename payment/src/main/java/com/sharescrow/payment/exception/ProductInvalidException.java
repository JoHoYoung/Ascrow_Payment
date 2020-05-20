package com.sharescrow.payment.exception;

import com.sharescrow.payment.ErrorCode;

public class ProductInvalidException extends BusinessException {
	public ProductInvalidException(ErrorCode errorCode) {
		super(errorCode);
	}
}
