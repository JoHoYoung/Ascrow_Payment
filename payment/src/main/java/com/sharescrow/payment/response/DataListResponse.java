package com.sharescrow.payment.response;

import lombok.Data;

import java.util.List;

@Data
public class DataListResponse<T> extends BaseResponse {
	private List<T> data;

	public DataListResponse(int statusCode, String statusMsg, List<T> data) {
		super(statusCode, statusMsg);
		this.data = data;
	}
}
