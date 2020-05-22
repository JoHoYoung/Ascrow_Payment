package com.sharescrow.payment.response;

import lombok.Data;

import java.util.List;

@Data
public class DataListResponse<T> extends BaseResponse {
	private List<T> data;

	public DataListResponse(List<T> data){
		super();
		this.data = data;
	}

	public DataListResponse(int statusCode, String statusMsg, List<T> data) {
		super(statusCode, statusMsg);
		this.data = data;
	}

}
