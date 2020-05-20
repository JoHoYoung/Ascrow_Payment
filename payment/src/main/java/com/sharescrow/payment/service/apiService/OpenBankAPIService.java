package com.sharescrow.payment.service.apiService;

import com.sharescrow.payment.ErrorCode;
import com.sharescrow.payment.context.pay.request.openbank.OpenBankApiCancelRequest;
import com.sharescrow.payment.context.pay.request.openbank.OpenBankApiRequest;
import com.sharescrow.payment.context.pay.response.openbank.OpenBankApiCancelResponse;
import com.sharescrow.payment.context.pay.response.openbank.OpenBankApiResponse;
import com.sharescrow.payment.exception.TransactionFailException;
import com.sharescrow.payment.service.apiService.uri.OpenBankURI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Service
// open banking error case :  only http Status Code
public class OpenBankAPIService {

	@Autowired
	RestTemplate openBankAPIRestTemplate;

	// error 가능
	public OpenBankApiResponse execute(OpenBankApiRequest openBankApiRequest) {
		try {
			ResponseEntity<OpenBankApiResponse> response = openBankAPIRestTemplate
				.postForEntity(OpenBankURI.APPROVE.getEndPoint(), openBankApiRequest, OpenBankApiResponse.class);
			return response.getBody();
		} catch (HttpServerErrorException e) {
			throw new TransactionFailException(ErrorCode.FAIL_PAYMENT_TRANSACTION);
		} catch (HttpClientErrorException e) {
			throw new TransactionFailException(ErrorCode.FAIL_PAYMENT_TRANSACTION);
		}
	}

	public OpenBankApiCancelResponse cancel(OpenBankApiCancelRequest openBankApiCancelRequest) {
		try {
			ResponseEntity<OpenBankApiCancelResponse> response = openBankAPIRestTemplate
				.postForEntity(OpenBankURI.CANCEL.getEndPoint(), openBankApiCancelRequest,
					OpenBankApiCancelResponse.class);
			return response.getBody();
		} catch (HttpServerErrorException e) {
			throw new TransactionFailException(ErrorCode.FAIL_PAYMENT_TRANSACTION);
		} catch (HttpClientErrorException e) {
			throw new TransactionFailException(ErrorCode.FAIL_PAYMENT_TRANSACTION);
		}
	}

}


