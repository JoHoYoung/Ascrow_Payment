package com.sharescrow.payment.service.apiService;

import com.sharescrow.payment.ErrorCode;
import com.sharescrow.payment.context.pay.request.naver.NaverPayApiApproveRequest;
import com.sharescrow.payment.context.pay.request.naver.NaverPayApiCancelRequest;
import com.sharescrow.payment.context.pay.request.naver.NaverPayApiReadyRequest;
import com.sharescrow.payment.context.pay.response.naver.NaverPayApiApproveResponse;
import com.sharescrow.payment.context.pay.response.naver.NaverPayApiCancelResponse;
import com.sharescrow.payment.context.pay.response.naver.NaverPayApiReadyResponse;
import com.sharescrow.payment.exception.TransactionFailException;
import com.sharescrow.payment.service.apiService.uri.NaverPayURI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class NaverPayApiService {

	@Autowired
	RestTemplate naverPayAPIRestTemplate;

	public NaverPayApiReadyResponse ready(NaverPayApiReadyRequest naverPayApiReadyRequest) {
		NaverPayApiReadyResponse naverPayApiReadyResponse = new NaverPayApiReadyResponse();
		naverPayApiReadyRequest.setReturnUrl(naverPayApiReadyRequest.getReturnUrl());
		return naverPayApiReadyResponse;
	}

	public NaverPayApiApproveResponse approve(NaverPayApiApproveRequest naverPayApiApproveRequest) throws
		HttpClientErrorException {
		try {
			ResponseEntity<NaverPayApiApproveResponse> response = naverPayAPIRestTemplate
				.postForEntity(NaverPayURI.APPROVE.getEndPoint(), naverPayApiApproveRequest,
					NaverPayApiApproveResponse.class);
			if (response.getBody().getCode() != "Success") {
				throw new TransactionFailException(ErrorCode.FAIL_PAYMENT_TRANSACTION);
			}
			return response.getBody();
		} catch (HttpServerErrorException e) {
			throw new TransactionFailException(ErrorCode.FAIL_PAYMENT_TRANSACTION);
		} catch (HttpClientErrorException e) {
			throw new TransactionFailException(ErrorCode.FAIL_PAYMENT_TRANSACTION);
		}
	}

	public NaverPayApiCancelResponse cancel(NaverPayApiCancelRequest naverPayApiCancelRequest) {
		ResponseEntity<NaverPayApiCancelResponse> response = naverPayAPIRestTemplate
			.postForEntity(NaverPayURI.CANCEL.getEndPoint(), naverPayApiCancelRequest, NaverPayApiCancelResponse.class);
		return response.getBody();
	}

}
