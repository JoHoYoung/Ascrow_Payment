package com.sharescrow.payment.service.apiService;

import com.sharescrow.payment.ErrorCode;
import com.sharescrow.payment.context.pay.request.kakao.KakaoPayApiApproveRequest;
import com.sharescrow.payment.context.pay.request.kakao.KakaoPayApiCancelRequest;
import com.sharescrow.payment.context.pay.request.kakao.KakaoPayApiReadyRequest;
import com.sharescrow.payment.context.pay.response.kakao.KakaoPayApiApproveCardResponse;
import com.sharescrow.payment.context.pay.response.kakao.KakaoPayApiCancelResponse;
import com.sharescrow.payment.context.pay.response.kakao.KakaoPayApiReadyResponse;
import com.sharescrow.payment.exception.PayReadyException;
import com.sharescrow.payment.exception.TransactionFailException;
import com.sharescrow.payment.service.apiService.uri.KakaoPayURI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class KakaoPayApiService {

	@Autowired
	RestTemplate kakaoPayAPIRestTemplate;

	public KakaoPayApiReadyResponse ready(KakaoPayApiReadyRequest kakaoPayApiReadyRequest){
		try {
			ResponseEntity<KakaoPayApiReadyResponse> response = kakaoPayAPIRestTemplate.postForEntity(
				KakaoPayURI.READY.getEndPoint(), kakaoPayApiReadyRequest, KakaoPayApiReadyResponse.class);
			return response.getBody();
		} catch (HttpServerErrorException e) {
			throw new PayReadyException(ErrorCode.FAIL_PAYMENT_TRANSACTION);
		} catch (HttpClientErrorException e) {
			throw new PayReadyException(ErrorCode.FAIL_PAYMENT_TRANSACTION);
		}
	}

	public KakaoPayApiApproveCardResponse approve(KakaoPayApiApproveRequest kakaoPayApiApproveRequest) {
		try {
			ResponseEntity<KakaoPayApiApproveCardResponse> response = kakaoPayAPIRestTemplate.postForEntity(
				KakaoPayURI.APPROVE.getEndPoint(), kakaoPayApiApproveRequest, KakaoPayApiApproveCardResponse.class);
			return response.getBody();
		} catch (HttpServerErrorException e) {
			throw new TransactionFailException(ErrorCode.FAIL_PAYMENT_TRANSACTION);
		} catch (HttpClientErrorException e) {
			throw new TransactionFailException(ErrorCode.FAIL_PAYMENT_TRANSACTION);
		}
	}

	public KakaoPayApiCancelResponse cancel(KakaoPayApiCancelRequest kaKaoPayApiCancelRequest) {
		ResponseEntity<KakaoPayApiCancelResponse> response = kakaoPayAPIRestTemplate.postForEntity(
			KakaoPayURI.CANCEL.getEndPoint(), kaKaoPayApiCancelRequest, KakaoPayApiCancelResponse.class);
		return response.getBody();
	}
}
