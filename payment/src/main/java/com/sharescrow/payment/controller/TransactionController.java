package com.sharescrow.payment.controller;

import com.sharescrow.payment.context.HistoryStage;
import com.sharescrow.payment.model.DataState;
import com.sharescrow.payment.model.Order;
import com.sharescrow.payment.model.Transaction;
import com.sharescrow.payment.response.BaseResponse;
import com.sharescrow.payment.response.DataResponse;
import com.sharescrow.payment.service.HistoryService;
import com.sharescrow.payment.service.OrderService;
import com.sharescrow.payment.service.TransactionService;
import com.sharescrow.payment.service.apiService.ProductApiService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transaction")
public class TransactionController {

	@Autowired
	HistoryService historyService;
	@Autowired
	OrderService orderService;
	@Autowired
	TransactionService transactionService;
	@Autowired
	ProductApiService productApiService;

	// in kakaopay, when user cancel call this
	@Transactional
	@PostMapping("/kakaoPay/cancel/{transactionId}")
	public ResponseEntity<BaseResponse> kakaoPayCancel(@PathVariable("transactionId")String transactionId) {
		Order order = orderService.getOrderByTransactionId(transactionId);
		Transaction transaction = transactionService.getTransactionById(order.getTransactionId());

		historyService.saveHistory(order, HistoryStage.TRANSACTION_CANCEL_DONE);

		transaction.setState(DataState.DELETED);
		transaction.setState(DataState.DELETED);
		transactionService.update(transaction.getId(), transaction);
		orderService.updateOrder(order);

		productApiService.cancelOrder(order);
		return new ResponseEntity<>(new BaseResponse(), HttpStatus.OK);
	}

	// in kakaopay, when failure call this

	@Transactional
	@PostMapping("/kakaoPay/fail/{transactionId}")
	public ResponseEntity<BaseResponse> kakaoPayFailure(@PathVariable("transactionId")String transactionId) {
		Order order = orderService.getOrderByTransactionId(transactionId);
		Transaction transaction = transactionService.getTransactionById(order.getTransactionId());

		historyService.saveHistory(order, HistoryStage.TRANSACTION_CANCEL_DONE);

		transaction.setState(DataState.DELETED);
		order.setState(DataState.DELETED);

		orderService.updateOrder(order);
		transactionService.update(transaction.getId(), transaction);

		productApiService.cancelOrder(order);
		return new ResponseEntity<>(new BaseResponse(), HttpStatus.OK);
	}

	// group's all users confirm order or not
	@GetMapping("/status/group")
	public ResponseEntity<BaseResponse> groupTransactionStatus(@RequestParam("groupId") int groupId,
		@RequestParam("numberOfMember") int numberOfmember) {
		int numberOfConfirmed = historyService.getConfirmedNumber(groupId);
		return new ResponseEntity<>(new DataResponse<>(200, "success", numberOfConfirmed == numberOfmember), HttpStatus.OK);
	}

}
