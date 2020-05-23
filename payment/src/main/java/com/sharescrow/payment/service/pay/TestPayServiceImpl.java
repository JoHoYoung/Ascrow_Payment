package com.sharescrow.payment.service.pay;

import com.sharescrow.payment.exception.ErrorCode;
import com.sharescrow.payment.context.HistoryStage;
import com.sharescrow.payment.exception.BusinessException;
import com.sharescrow.payment.model.DataState;
import com.sharescrow.payment.model.Order;
import com.sharescrow.payment.model.Transaction;
import com.sharescrow.payment.response.BaseResponse;
import com.sharescrow.payment.service.HistoryService;
import com.sharescrow.payment.service.OrderService;
import com.sharescrow.payment.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class TestPayServiceImpl implements PayService<Order, Order> {

	@Autowired
	ObjectMapper objectMapper;
	@Autowired
	TransactionService transactionService;
	@Autowired
	OrderService orderService;
	@Autowired
	HistoryService historyService;

	public Order execute(Order order) {
		// # TODO product Valid call

		historyService.saveHistory(order, HistoryStage.TRANSACTION_PENDING);
		Transaction transaction = new Transaction();
		transaction.setTransactionKey(UUID.randomUUID().toString());
		transaction.setTransactionAmount(5000);
		transaction.setState(DataState.CREATED);
		order.setTransactionId(transaction.getId());
		order.setState(DataState.CREATED);
		transactionService.creatTransaction(transaction);
		orderService.createOrder(order);
		historyService.saveHistory(order, HistoryStage.TRANSACTION_DONE);
		return order;
		// # TODO Enqueue

	}

	public BaseResponse approve(String transactionId, Map<String, String> params) {
		throw new BusinessException(ErrorCode.UNSUPPORTED_OPERATION);
	}

	public Order cancel(Order order, Transaction transaction) {
		try {
			historyService.saveHistory(order, HistoryStage.ORDER_CANCEL_PENDING);
			historyService.saveHistory(order, HistoryStage.ORDER_CANCEL_DONE);

			transaction.setState(DataState.DELETED);
			order.setState(DataState.DELETED);
			transactionService.update(transaction.getId(), transaction);
			orderService.updateOrder(order);
			return order;
		} catch (BusinessException e) {
			throw new BusinessException(ErrorCode.TRANASACTION_CANCEL_FAIL);
		}
	}
}
