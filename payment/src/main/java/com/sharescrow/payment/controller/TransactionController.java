package com.sharescrow.payment.controller;

import com.sharescrow.payment.model.Order;
import com.sharescrow.payment.response.BaseResponse;
import com.sharescrow.payment.response.DataResponse;
import com.sharescrow.payment.service.HistoryService;
import com.sharescrow.payment.service.OrderService;
import com.sharescrow.payment.service.apiService.ProductApiService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	ProductApiService productApiService;

	// in kakaopay, when user cancel call this
	@PostMapping("/kakaoPay/cancel/{transactionId}")
	public ResponseEntity<BaseResponse> kakaoPayCancel(@PathVariable("transactionId")String transactionId) {
		Order order = orderService.getOrderByTransactionId(transactionId);
		productApiService.cancelOrder(order);
		return new ResponseEntity<>(new BaseResponse(200,"success"), HttpStatus.OK);
	}

	// in kakaopay, when failure call this
	@PostMapping("/kakaoPay/fail/{transactionId}")
	public ResponseEntity<BaseResponse> kakaoPayFailure(@PathVariable("transactionId")String transactionId) {
		Order order = orderService.getOrderByTransactionId(transactionId);
		productApiService.cancelOrder(order);
		return new ResponseEntity<>(new BaseResponse(200,"success"), HttpStatus.OK);
	}

	// group's all users confirm order or not
	@GetMapping("/status/group")
	public ResponseEntity<BaseResponse> groupTransactionStatus(@RequestParam("groupId") int groupId,
		@RequestParam("numberOfMember") int numberOfmember) {
		int numberOfConfirmed = historyService.getConfirmedNumber(groupId);
		boolean result = numberOfConfirmed == numberOfmember;
		return new ResponseEntity<>(new DataResponse<>(200, "success", result), HttpStatus.OK);
	}

}
