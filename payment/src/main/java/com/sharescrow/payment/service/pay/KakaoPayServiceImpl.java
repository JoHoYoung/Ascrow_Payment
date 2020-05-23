package com.sharescrow.payment.service.pay;

import com.sharescrow.payment.exception.ErrorCode;
import com.sharescrow.payment.context.HistoryStage;
import com.sharescrow.payment.context.pay.response.kakao.KakaoPayApiApproveCardResponse;
import com.sharescrow.payment.context.pay.response.kakao.KakaoPayApiCancelResponse;
import com.sharescrow.payment.context.pay.response.kakao.KakaoPayApiReadyResponse;
import com.sharescrow.payment.exception.BusinessException;
import com.sharescrow.payment.model.Order;
import com.sharescrow.payment.model.Transaction;
import com.sharescrow.payment.service.HistoryService;
import com.sharescrow.payment.service.OrderService;
import com.sharescrow.payment.service.TransactionService;
import com.sharescrow.payment.service.apiService.KakaoPayApiService;
import com.sharescrow.payment.service.apiService.ProductApiService;
import com.sharescrow.payment.service.apiService.QueueApiService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Service
public class KakaoPayServiceImpl
	implements PayService<KakaoPayApiReadyResponse, KakaoPayApiCancelResponse> {

	private final Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	ObjectMapper objectMapper;
	@Autowired
	TransactionService transactionService;
	@Autowired
	OrderService orderService;
	@Autowired
	KakaoPayApiService kakaoPayApiService;
	@Autowired
	ProductApiService productApiService;
	@Autowired
	HistoryService historyService;
	@Autowired
	QueueApiService queueApiService;
	@Autowired
	DataSourceTransactionManager dataSourceTransactionManager;

	// call kakao pay ready api
	// 1. product valid call
	// 2. kakao pay ready call
	// if 1 fails, its error dealt at productApiService Class and nothing to do
	// but if 2 fails, must call product's cancel api for recovering stock
	@Transactional
	public KakaoPayApiReadyResponse execute(Order order) {
		// JsonProcessingException Point
		// is valid order?
		try {
			KakaoPayApiReadyResponse kakaoPayApiReadyResponse = kakaoPayApiService.ready(order);
			historyService.saveHistory(order, HistoryStage.TRANSACTION_PENDING);
			return kakaoPayApiReadyResponse;
		} catch (BusinessException e) {
			// call product for cancel
			// Async Call
			logger.error("Kakao Pay Ready Error order Id : ".concat(Integer.toString(order.getId())));
			productApiService.cancelOrder(order);
			throw new BusinessException(ErrorCode.FAIL_PAYMENT_TRANSACTION);
		}
	}

	// call kakao pay approve api
	// 1. if success, enque and save snapshot
	// 2. if fails,

	@Transactional
	public KakaoPayApiApproveCardResponse approve(String transactionId, String pg_token) {
		Order order = orderService.getOrderByTransactionId(transactionId);
		try {
			// BusinessException Point
			KakaoPayApiApproveCardResponse kakaoPayApiApproveCardResponse = kakaoPayApiService.approve(order,
				pg_token);
			// save snapshot
			historyService.saveHistory(order, HistoryStage.TRANSACTION_DONE);
			// Async enqueue
			queueApiService.enqueue(order);
			return kakaoPayApiApproveCardResponse;
		} catch (BusinessException e) {
			// Async Call
			logger.error("Kakao Pay Approve Error order Id : ".concat(Integer.toString(order.getId())));
			productApiService.cancelOrder(order);
			throw new BusinessException(ErrorCode.FAIL_PAYMENT_TRANSACTION);
			// if transaction not exist
		}
	}

	public KakaoPayApiCancelResponse cancel(Order order, Transaction transaction) {
		historyService.saveHistory(order, HistoryStage.TRANSACTION_CANCEL_PENDING);
		TransactionStatus txStatus =
			dataSourceTransactionManager.getTransaction(new DefaultTransactionDefinition());
		try {
			dataSourceTransactionManager.rollback(txStatus);
			KakaoPayApiCancelResponse kakaoPayApiCancelResponse = kakaoPayApiService.cancel(order, transaction);
			historyService.saveHistory(order, HistoryStage.TRANSACTION_CANCEL_DONE);
			return kakaoPayApiCancelResponse;
		} catch (BusinessException e) {
			dataSourceTransactionManager.rollback(txStatus);
			logger.error("Internal Server Error : ".concat(Integer.toString(order.getId()))
				.concat("MSG : ")
				.concat(e.getMessage()));
			throw new BusinessException(ErrorCode.TRANASACTION_CANCEL_FAIL);
		}

	}

}