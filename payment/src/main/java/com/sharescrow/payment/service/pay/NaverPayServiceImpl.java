package com.sharescrow.payment.service.pay;

import com.sharescrow.payment.exception.ErrorCode;
import com.sharescrow.payment.context.HistoryStage;
import com.sharescrow.payment.context.pay.response.naver.NaverPayApiApproveResponse;
import com.sharescrow.payment.context.pay.response.naver.NaverPayApiCancelResponse;
import com.sharescrow.payment.context.pay.response.naver.NaverPayApiReadyResponse;
import com.sharescrow.payment.exception.*;
import com.sharescrow.payment.model.Order;
import com.sharescrow.payment.model.Transaction;
import com.sharescrow.payment.service.HistoryService;
import com.sharescrow.payment.service.OrderService;
import com.sharescrow.payment.service.TransactionService;
import com.sharescrow.payment.service.apiService.NaverPayApiService;
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
public class NaverPayServiceImpl implements PayService<NaverPayApiReadyResponse, NaverPayApiCancelResponse> {

	private final Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	OrderService orderService;
	@Autowired
	TransactionService transactionService;
	@Autowired
	ObjectMapper objectMapper;
	@Autowired
	NaverPayApiService naverPayApiService;
	@Autowired
	ProductApiService productApiService;
	@Autowired
	HistoryService historyService;
	@Autowired
	QueueApiService queueApiService;
	@Autowired
	DataSourceTransactionManager dataSourceTransactionManager;

	// before start naver pay, save transaction to db
	// in ready status of naver pay, not use network(dont call api), process of set internal call back uri
	// from naver pay after ready
	@Transactional
	public NaverPayApiReadyResponse execute(Order order) {
		// no api call, but conceptual
		NaverPayApiReadyResponse naverPayApiReadyResponse = naverPayApiService.ready(order);
		historyService.saveHistory(order, HistoryStage.TRANSACTION_PENDING);
		return naverPayApiReadyResponse;
	}

	// naver pay call this function
	// after get call, call naverpay approve uril
	// both Cancel and Fail call /api/v1/order/transaction/done/{payType}/{transactionId}
	// handle if code is Cancel or Fail
	// first and last call to naver pay, other process in client side(conceptually)
	@Transactional
	public NaverPayApiApproveResponse approve(String transactionId, String resultCode, String paymentId) {
		if (resultCode != "Success") {
			throw new BusinessException(ErrorCode.FAIL_PAYMENT_TRANSACTION);
		}
		Order order = orderService.getOrderByTransactionId(transactionId);
		try {
			NaverPayApiApproveResponse naverPayApiApproveResponse = naverPayApiService.approve(order, paymentId);
			// save snapshot
			historyService.saveHistory(order, HistoryStage.TRANSACTION_DONE);
			// Async enqueue
			queueApiService.enqueue(order);
			return naverPayApiApproveResponse;
		} catch (BusinessException e) {
			// call product for cancel
			// Async Call
			productApiService.cancelOrder(order);
			logger.error("Naver Pay Approve Error order Id : ".concat(Integer.toString(order.getId())));
			throw new BusinessException(ErrorCode.FAIL_PAYMENT_TRANSACTION);
			//  if transaction not exist
		}
	}

	public NaverPayApiCancelResponse cancel(Order order, Transaction transaction) {
		historyService.saveHistory(order, HistoryStage.TRANSACTION_CANCEL_PENDING);
		TransactionStatus txStatus =
			dataSourceTransactionManager.getTransaction(new DefaultTransactionDefinition());
		try {
			NaverPayApiCancelResponse naverPayApiCancelResponse = naverPayApiService.cancel(transaction, order);
			historyService.saveHistory(order, HistoryStage.TRANSACTION_CANCEL_DONE);
			return naverPayApiCancelResponse;
		} catch (BusinessException e) {
			dataSourceTransactionManager.rollback(txStatus);
			logger.error("Naver Pay Transaction Cancel Error order Id : ".concat(Integer.toString(order.getId())));
			throw new BusinessException(ErrorCode.TRANASACTION_CANCEL_FAIL);
		} catch (Exception e) {
			dataSourceTransactionManager.rollback(txStatus);
			logger.error("Internal Server Error : ".concat(Integer.toString(order.getId()))
				.concat("MSG : ")
				.concat(e.getMessage()));
			throw new BusinessException(ErrorCode.TRANASACTION_CANCEL_FAIL);
		}
	}

}
