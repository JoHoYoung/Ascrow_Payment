package com.sharescrow.payment.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataResponse<T> extends BaseResponse {
	public T data;

	public DataResponse(T data){
		this.data = data;
	}

	public DataResponse(int statusCode, String statusMsg, T data) {
		super(statusCode, statusMsg);
		this.data = data;
	}

}
