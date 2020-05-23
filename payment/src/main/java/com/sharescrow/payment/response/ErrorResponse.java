package com.sharescrow.payment.response;

import com.sharescrow.payment.exception.ErrorCode;
import lombok.Data;

@Data
public class ErrorResponse extends BaseResponse {
	private String code;

	public ErrorResponse(ErrorCode errorCode) {
		super(errorCode.getStatus(), errorCode.getStatusMsg());
		this.code = errorCode.getStatusCode();
	}
}

