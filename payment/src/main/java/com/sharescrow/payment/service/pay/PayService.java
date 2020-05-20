package com.sharescrow.payment.service.pay;

import com.sharescrow.payment.response.BaseResponse;
import com.sharescrow.payment.response.DataResponse;

import java.util.Map;

public interface PayService {
	DataResponse execute(String params);

	BaseResponse approve(String transactionId, Map<String, String> params);
}
