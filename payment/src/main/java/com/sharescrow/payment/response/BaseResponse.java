package com.sharescrow.payment.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class BaseResponse {
	private int statusCode;
	private String statusMsg;

	public BaseResponse(){
		this.statusCode = 200;
		this.statusMsg = "success";
	}
}
