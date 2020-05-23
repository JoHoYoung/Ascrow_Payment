package com.sharescrow.payment.service.apiService;

import com.sharescrow.payment.exception.ErrorCode;
import com.sharescrow.payment.context.pay.request.kakao.KakaoPayApiApproveRequest;
import com.sharescrow.payment.context.pay.request.kakao.KakaoPayApiCancelRequest;
import com.sharescrow.payment.context.pay.request.kakao.KakaoPayApiReadyRequest;
import com.sharescrow.payment.context.pay.response.kakao.KakaoPayApiApproveCardResponse;
import com.sharescrow.payment.context.pay.response.kakao.KakaoPayApiCancelResponse;
import com.sharescrow.payment.context.pay.response.kakao.KakaoPayApiReadyResponse;
import com.sharescrow.payment.context.product.response.ProductValidResponse;
import com.sharescrow.payment.exception.BusinessException;
import com.sharescrow.payment.model.DataState;
import com.sharescrow.payment.model.Order;
import com.sharescrow.payment.model.Transaction;
import com.sharescrow.payment.service.OrderService;
import com.sharescrow.payment.service.TransactionService;
import com.sharescrow.payment.service.apiService.uri.KakaoPayURI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class KakaoPayApiService {

	@Autowired
	RestTemplate kakaoPayAPIRestTemplate;
	@Autowired
	TransactionService transactionService;
	@Autowired
	ProductApiService productApiService;
	@Autowired
	OrderService orderService;

	public KakaoPayApiReadyResponse callReadyApi(KakaoPayApiReadyRequest kakaoPayApiReadyRequest) {
		ResponseEntity<KakaoPayApiReadyResponse> response = kakaoPayAPIRestTemplate.postForEntity(
			KakaoPayURI.READY.getEndPoint(), kakaoPayApiReadyRequest, KakaoPayApiReadyResponse.class);
		return response.getBody();
	}

	public KakaoPayApiReadyResponse ready(Order order) {
		try {

			ProductValidResponse productValidResponse = productApiService.isValidOrder(order);
			Transaction transaction = new Transaction();
			// save snapshot
			KakaoPayApiReadyRequest kakaoPayApiReadyRequest = KakaoPayApiReadyRequest.builder()
				.item_name(productValidResponse.getProductName())
				.total_amount(productValidResponse.getTransAmount())
				.quantity(order.getQuantitiy())
				.approval_url(KakaoPayURI.DONE_CALLBACK.getBaseUri()
					.concat(KakaoPayURI.DONE_CALLBACK.getEndPoint())
					.concat(transaction.getId()))
				.cancel_url(KakaoPayURI.CANCEL_CALLBACK.getBaseUri()
					.concat(KakaoPayURI.DONE_CALLBACK.getEndPoint())
					.concat(transaction.getId()))
				.fail_url(KakaoPayURI.FAIL_CALLBACK.getBaseUri()
					.concat(KakaoPayURI.DONE_CALLBACK.getEndPoint())
					.concat(transaction.getId())).build();
			// BusinessException Point
			KakaoPayApiReadyResponse kakaoPayApiReadyResponse = this.callReadyApi(kakaoPayApiReadyRequest);
			// if OK
			transaction.setPlatform("KAKAO_PAY");
			transaction.setTransactionKey(kakaoPayApiReadyResponse.getTid());
			transaction.setTransactionAmount(productValidResponse.getTransAmount());
			transaction.setState(DataState.TEMPORARY);

			order.setTransactionId(transaction.getId());
			order.setState(DataState.CREATED);
			// save data

			transactionService.creatTransaction(transaction);
			orderService.createOrder(order);
			return kakaoPayApiReadyResponse;
		} catch (HttpServerErrorException e) {
			throw new BusinessException(ErrorCode.FAIL_PAYMENT_TRANSACTION);
		} catch (HttpClientErrorException e) {
			throw new BusinessException(ErrorCode.FAIL_PAYMENT_TRANSACTION);
		}
	}

	public KakaoPayApiApproveCardResponse callApproveApi(KakaoPayApiApproveRequest kakaoPayApiApproveRequest) {
		ResponseEntity<KakaoPayApiApproveCardResponse> response = kakaoPayAPIRestTemplate.postForEntity(
			KakaoPayURI.APPROVE.getEndPoint(), kakaoPayApiApproveRequest, KakaoPayApiApproveCardResponse.class);
		return response.getBody();

	}

	public KakaoPayApiApproveCardResponse approve(Order order, String pg_token) {
		try {
			Transaction transaction = transactionService.getTransactionById(order.getTransactionId());
			KakaoPayApiApproveRequest kakaoPayApiApproveRequest = KakaoPayApiApproveRequest.builder()
				.tid(transaction.getTransactionKey())
				.pg_token(pg_token).build();

			// BusinessException Point
			KakaoPayApiApproveCardResponse kakaoPayApiApproveCardResponse = this.callApproveApi(kakaoPayApiApproveRequest);

			transaction.setState(DataState.CREATED);

			transactionService.update(order.getTransactionId(), transaction);
			return kakaoPayApiApproveCardResponse;
		} catch (HttpServerErrorException e) {
			throw new BusinessException(ErrorCode.FAIL_PAYMENT_TRANSACTION);
		} catch (HttpClientErrorException e) {
			throw new BusinessException(ErrorCode.FAIL_PAYMENT_TRANSACTION);
		}
	}

	public KakaoPayApiCancelResponse callCancelApi(KakaoPayApiCancelRequest kaKaoPayApiCancelRequest) {
		ResponseEntity<KakaoPayApiCancelResponse> response = kakaoPayAPIRestTemplate.postForEntity(
			KakaoPayURI.CANCEL.getEndPoint(), kaKaoPayApiCancelRequest, KakaoPayApiCancelResponse.class);
		return response.getBody();
	}

	@Transactional
	public KakaoPayApiCancelResponse cancel(Order order, Transaction transaction) {
		try {
			KakaoPayApiCancelRequest kakaoPayApiCancelRequest = KakaoPayApiCancelRequest.builder()
				.cancel_amount(transaction.getTransactionAmount())
				.tid(transaction.getTransactionKey()).build();
			KakaoPayApiCancelResponse kakaoPayApiCancelResponse = this.callCancelApi(kakaoPayApiCancelRequest);

			transaction.setState(DataState.DELETED);
			order.setState(DataState.DELETED);

			transactionService.update(transaction.getId(), transaction);
			orderService.updateOrder(order);
			return kakaoPayApiCancelResponse;
		} catch (HttpServerErrorException e) {
			throw new BusinessException(ErrorCode.FAIL_PAYMENT_TRANSACTION);
		} catch (HttpClientErrorException e) {
			throw new BusinessException(ErrorCode.FAIL_PAYMENT_TRANSACTION);
		}
	}
}
