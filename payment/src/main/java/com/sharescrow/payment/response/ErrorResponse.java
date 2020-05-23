package com.sharescrow.payment.response;

import com.sharescrow.payment.exception.ErrorCode;
import lombok.Data;

@Data
public class ErrorResponse extends BaseResponse {
	public ErrorResponse(ErrorCode errorCode) {
		super(errorCode.getStatus(), errorCode.getStatusMsg());
	}
}

