package com.sharescrow.payment.service.pay;

import com.sharescrow.payment.ErrorCode;
import com.sharescrow.payment.context.HistoryStage;
import com.sharescrow.payment.context.pay.request.naver.NaverPayApiCancelRequest;
import com.sharescrow.payment.context.pay.request.openbank.OpenBankApiCancelRequest;
import com.sharescrow.payment.context.pay.request.openbank.OpenBankApiRequest;
import com.sharescrow.payment.context.pay.response.naver.NaverPayApiCancelResponse;
import com.sharescrow.payment.context.pay.response.openbank.OpenBankApiCancelResponse;
import com.sharescrow.payment.context.pay.response.openbank.OpenBankApiResponse;
import com.sharescrow.payment.context.product.response.ProductValidResponse;
import com.sharescrow.payment.exception.InternalServerException;
import com.sharescrow.payment.exception.InvalidOrderRequestException;
import com.sharescrow.payment.exception.TransactionCancelFailException;
import com.sharescrow.payment.exception.TransactionFailException;
import com.sharescrow.payment.exception.UnSupportedOperationException;
import com.sharescrow.payment.model.DataState;
import com.sharescrow.payment.model.Order;
import com.sharescrow.payment.model.Transaction;
import com.sharescrow.payment.response.BaseResponse;
import com.sharescrow.payment.response.DataResponse;
import com.sharescrow.payment.service.HistoryService;
import com.sharescrow.payment.service.OrderService;
import com.sharescrow.payment.service.TransactionService;
import com.sharescrow.payment.service.apiService.OpenBankAPIService;
import com.sharescrow.payment.service.apiService.ProductApiService;
import com.sharescrow.payment.service.apiService.QueueApiService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.Map;

@Service
public class OpenbankPayServiceImpl implements PayService<OpenBankApiResponse, OpenBankApiCancelResponse> {

	private final Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	ObjectMapper objectMapper;
	@Autowired
	OpenBankAPIService openBankAPIService;
	@Autowired
	ProductApiService productApiService;
	@Autowired
	TransactionService transactionService;
	@Autowired
	OrderService orderService;
	@Autowired
	HistoryService historyService;
	@Autowired
	QueueApiService queueApiService;
	@Autowired
	KakaoPayServiceImpl kakaoPayService;
	@Autowired
	NaverPayServiceImpl naverPayService;
	@Autowired
	DataSourceTransactionManager dataSourceTransactionManager;

	@Transactional
	public OpenBankApiResponse execute(Order order) {
		try {
			// TransactionFailException Point
			OpenBankApiResponse openBankApiResponse = openBankAPIService.execute(order);
			historyService.saveHistory(order, HistoryStage.TRANSACTION_DONE);
			// Async enqueue
			queueApiService.enqueue(order);
			return openBankApiResponse;
		} catch (TransactionFailException e) {
			// call product for cancel
			// Async Call
			productApiService.cancelOrder(order);
			throw new TransactionFailException(ErrorCode.FAIL_PAYMENT_TRANSACTION);
		}
	}

	public OpenBankApiCancelResponse cancel(Order order, Transaction transaction) {

		historyService.saveHistory(order, HistoryStage.ORDER_CANCEL_PENDING);
		TransactionStatus txStatus =
			dataSourceTransactionManager.getTransaction(new DefaultTransactionDefinition());
		try {
			OpenBankApiCancelResponse openBankApiCancelResponse = openBankAPIService.cancel(order, transaction);
			historyService.saveHistory(order, HistoryStage.TRANSACTION_CANCEL_DONE);
			return openBankApiCancelResponse;
		} catch (TransactionCancelFailException e) {
			dataSourceTransactionManager.rollback(txStatus);
			logger.error("OpenBank Transaction Cancel Error order Id : ".concat(Integer.toString(order.getId())));
			throw new TransactionCancelFailException(ErrorCode.TRANASACTION_CANCEL_FAIL);
		} catch(Exception e){
			dataSourceTransactionManager.rollback(txStatus);
			logger.error("Internal Server error".concat(Integer.toString(order.getId())));
			throw new InternalServerException(ErrorCode.INTERNAL_SERVER_ERROR);
		}

	}
}
