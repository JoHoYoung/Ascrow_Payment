package com.sharescrow.payment.service.pay;

import com.sharescrow.payment.ErrorCode;
import com.sharescrow.payment.exception.InvalidPayTypeException;

import org.springframework.stereotype.Component;

@Component
public class PayServiceFactory {
	public PayService getPay(String payTypeString) {
		try {
			return PayType.valueOf(payTypeString).getPayService();
		} catch (IllegalArgumentException e) {
			throw new InvalidPayTypeException(ErrorCode.INVALID_PAYREQUEST_TYPE);
		}
	}
}
