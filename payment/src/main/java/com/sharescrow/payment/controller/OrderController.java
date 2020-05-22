package com.sharescrow.payment.controller;

import com.sharescrow.payment.model.Order;
import com.sharescrow.payment.response.BaseResponse;
import com.sharescrow.payment.response.DataListResponse;
import com.sharescrow.payment.service.HistoryService;
import com.sharescrow.payment.service.OrderService;
import com.sharescrow.payment.service.apiService.ProductApiService;
import com.sharescrow.payment.service.pay.PayServiceFactory;
import com.sharescrow.payment.service.pay.TestPayServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	OrderService orderService;
	@Autowired
	PayServiceFactory payServiceFactory;
	@Autowired
	ObjectMapper objectMapper;
	@Autowired
	ProductApiService productService;
	@Autowired
	TestPayServiceImpl testPayService;
	@Autowired
	HistoryService historyService;

	// get User's Order List
	@GetMapping("/list/user")
	public ResponseEntity<BaseResponse> list(@RequestParam("userId") int userId) {
		return new ResponseEntity<>(new DataListResponse(200, "success", orderService.getOrdersByUserId(userId)), HttpStatus.OK);
	}

	// do order
	@PostMapping("/execute")
	public ResponseEntity<BaseResponse> pay(@RequestParam("payType") String payType,
		@RequestBody String params) {
		return new ResponseEntity<>(payServiceFactory.getPay(payType).execute(params),
			HttpStatus.CREATED);
	}

	// transaction callback from naver pay, kakao pay
	@PostMapping("/transaction/done/{payType}/{transactionId}")
	public ResponseEntity<BaseResponse> done(@PathVariable("payType") String payType,
		@PathVariable("transactionId") String transactionId,
		@RequestParam Map<String, String> params) {
		return new ResponseEntity<>(payServiceFactory.getPay(payType).approve(transactionId, params)
			, HttpStatus.OK);
	}

	// order cancel, refund
	@PostMapping("/cancel/{payType}")
	public ResponseEntity<BaseResponse> cancel(@PathVariable("payType")String payType, @RequestBody String params){
		return new ResponseEntity<>(payServiceFactory.getPay(payType).cancel(params), HttpStatus.OK);
	}

	// order confirm
	@GetMapping("/confirm")
	public ResponseEntity<BaseResponse> confirmOrder(@RequestParam("orderId") int orderId) {
		Order order = orderService.getOrderById(orderId);
		historyService.orderConfirm(order);
		return new ResponseEntity<>(new BaseResponse(200, "success"), HttpStatus.OK);
	}

	//------------------------------------- for test ( because we can't actually do payment) -------------------------//
	@PostMapping("/execute/test")
	public ResponseEntity<BaseResponse> payTest(@RequestBody String params) {
		return new ResponseEntity<>(testPayService.execute(params)
		, HttpStatus.OK);
	}

	@PostMapping("/test")
	public ResponseEntity<BaseResponse> test() throws InterruptedException {
		final BaseResponse response = new BaseResponse(200, "success");
		Thread.sleep(2000);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/test2")
	public ResponseEntity<BaseResponse> test2() {
		productService.cancelOrder(new Order());
		BaseResponse response = new BaseResponse(200, "success");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
