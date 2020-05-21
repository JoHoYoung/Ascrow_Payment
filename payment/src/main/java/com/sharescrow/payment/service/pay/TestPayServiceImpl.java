package com.sharescrow.payment.service.pay;

import com.sharescrow.payment.ErrorCode;
import com.sharescrow.payment.exception.InvalidOrderRequestException;
import com.sharescrow.payment.exception.UnSupportedOperationException;
import com.sharescrow.payment.model.Order;
import com.sharescrow.payment.model.Transaction;
import com.sharescrow.payment.response.BaseResponse;
import com.sharescrow.payment.response.DataResponse;
import com.sharescrow.payment.service.HistoryService;
import com.sharescrow.payment.service.OrderService;
import com.sharescrow.payment.service.TransactionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class TestPayServiceImpl implements PayService {

	@Autowired
	ObjectMapper objectMapper;
	@Autowired
	TransactionService transactionService;
	@Autowired
	OrderService orderService;
	@Autowired
	HistoryService historyService;

	public DataResponse execute(String params) {
		try {
			Order order = objectMapper.readValue(params, Order.class);
			// # TODO product Valid call

			historyService.transactionStart(order);
			Transaction transaction = new Transaction();
			transaction.setTransactionKey(UUID.randomUUID().toString());
			transaction.setTransactionAmount(5000);
			transaction.setDoneState();
			order.setTransactionId(transaction.getId());
			order.setCreated();
			transactionService.creatTransaction(transaction);
			orderService.createOrder(order);
			historyService.transactionDone(order);
			return new DataResponse(200, "success", order);
			// # TODO Enqueue
		} catch (JsonProcessingException e) {
			throw new InvalidOrderRequestException(ErrorCode.INVALID_ORDER_DATA_FORMAT);
		}
	}

	public BaseResponse approve(String transactionId, Map<String, String> params) {
		throw new UnSupportedOperationException(ErrorCode.UNSUPPORTED_OPERATION);
	}

}
