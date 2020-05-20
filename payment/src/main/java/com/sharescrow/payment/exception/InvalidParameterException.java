package com.sharescrow.payment.exception;

import com.sharescrow.payment.ErrorCode;

public class InvalidParameterException extends BusinessException{
	public InvalidParameterException(ErrorCode errorCode){
		super(errorCode);
	}
}
