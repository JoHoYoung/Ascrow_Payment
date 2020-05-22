package com.sharescrow.payment.service.apiService;

import com.sharescrow.payment.ErrorCode;
import com.sharescrow.payment.context.pay.request.naver.NaverPayApiApproveRequest;
import com.sharescrow.payment.context.pay.request.naver.NaverPayApiCancelRequest;
import com.sharescrow.payment.context.pay.request.naver.NaverPayApiReadyRequest;
import com.sharescrow.payment.context.pay.response.naver.NaverPayApiApproveResponse;
import com.sharescrow.payment.context.pay.response.naver.NaverPayApiCancelResponse;
import com.sharescrow.payment.context.pay.response.naver.NaverPayApiReadyResponse;
import com.sharescrow.payment.context.product.response.ProductValidResponse;
import com.sharescrow.payment.exception.TransactionCancelFailException;
import com.sharescrow.payment.exception.TransactionFailException;
import com.sharescrow.payment.model.DataState;
import com.sharescrow.payment.model.Order;
import com.sharescrow.payment.model.Transaction;
import com.sharescrow.payment.service.OrderService;
import com.sharescrow.payment.service.TransactionService;
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
	OrderService orderService;
	@Autowired
	RestTemplate naverPayAPIRestTemplate;
	@Autowired
	NaverPayApiService naverPayApiService;
	@Autowired
	ProductApiService productApiService;
	@Autowired
	TransactionService transactionService;

	public NaverPayApiReadyResponse ready(Order order) {
		ProductValidResponse productValidResponse = productApiService.isValidOrder(order);

		Transaction transaction = new Transaction();
		transaction.setPlatform("NAVER_PAY");
		transaction.setTransactionAmount(productValidResponse.getTransAmount());
		transaction.setState(DataState.TEMPORARY);

		// set callback url not use network
		NaverPayApiReadyRequest naverPayApiReadyRequest = NaverPayApiReadyRequest.builder().returnUrl(
			NaverPayURI.CALL_BACK.getBaseUri()
				.concat(NaverPayURI.CALL_BACK.getEndPoint())
				.concat(transaction.getId())).build();

		order.setTransactionId(transaction.getId());
		order.setState(DataState.CREATED);
		// save to db
		transactionService.creatTransaction(transaction);
		orderService.createOrder(order);

		NaverPayApiReadyResponse naverPayApiReadyResponse = new NaverPayApiReadyResponse();
		naverPayApiReadyRequest.setReturnUrl(naverPayApiReadyRequest.getReturnUrl());
		return naverPayApiReadyResponse;
	}

	public NaverPayApiApproveResponse callApproveApi(NaverPayApiApproveRequest naverPayApiApproveRequest) throws HttpClientErrorException, HttpServerErrorException {
		ResponseEntity<NaverPayApiApproveResponse> response = naverPayAPIRestTemplate
			.postForEntity(NaverPayURI.APPROVE.getEndPoint(), naverPayApiApproveRequest,
				NaverPayApiApproveResponse.class);
		if (response.getBody().getCode() != "Success") {
			throw new TransactionFailException(ErrorCode.FAIL_PAYMENT_TRANSACTION);
		}
		return response.getBody();
	}

	public NaverPayApiApproveResponse approve(Order order, String paymentId) {
		try {
			Transaction transaction = transactionService.getTransactionById(order.getTransactionId());
			NaverPayApiApproveRequest naverPayApproveRequest = new NaverPayApiApproveRequest();
			naverPayApproveRequest.setPaymentId(paymentId);

			// TransactionFailException Point
			NaverPayApiApproveResponse naverPayApiApproveResponse = naverPayApiService.callApproveApi(
				naverPayApproveRequest);
			transaction.setTransactionKey(paymentId);
			transaction.setState(DataState.CREATED);
			transactionService.update(order.getTransactionId(), transaction);
			return naverPayApiApproveResponse;
		} catch (HttpServerErrorException e) {
			throw new TransactionFailException(ErrorCode.FAIL_PAYMENT_TRANSACTION);
		} catch (HttpClientErrorException e) {
			throw new TransactionFailException(ErrorCode.FAIL_PAYMENT_TRANSACTION);
		}
	}

	public NaverPayApiCancelResponse callCancelApi(NaverPayApiCancelRequest naverPayApiCancelRequest) throws HttpClientErrorException, HttpServerErrorException {
		ResponseEntity<NaverPayApiCancelResponse> response = naverPayAPIRestTemplate
			.postForEntity(NaverPayURI.CANCEL.getEndPoint(), naverPayApiCancelRequest,
				NaverPayApiCancelResponse.class);
		if (response.getBody().getCode() != "Success") {
			throw new TransactionFailException(ErrorCode.FAIL_PAYMENT_TRANSACTION);
		}
		return response.getBody();
	}

	public NaverPayApiCancelResponse cancel(Transaction transaction, Order order) {
		try {
			NaverPayApiCancelRequest naverPayApiCancelRequest = NaverPayApiCancelRequest.builder()
				.cancelAmount(transaction.getTransactionAmount())
				.paymentId(transaction.getTransactionKey()).build();
			NaverPayApiCancelResponse naverPayApiCancelResponse = this.callCancelApi(naverPayApiCancelRequest);

			transaction.setState(DataState.DELETED);
			order.setState(DataState.DELETED);
			transactionService.update(transaction.getId(), transaction);
			orderService.updateOrder(order);
			return naverPayApiCancelResponse;
		} catch (HttpServerErrorException e) {
			throw new TransactionCancelFailException(ErrorCode.FAIL_PAYMENT_TRANSACTION);
		} catch (HttpClientErrorException e) {
			throw new TransactionCancelFailException(ErrorCode.FAIL_PAYMENT_TRANSACTION);
		}
	}

}
