package com.sharescrow.payment.controller;

import com.sharescrow.payment.context.HistoryStage;
import com.sharescrow.payment.model.Order;
import com.sharescrow.payment.model.Transaction;
import com.sharescrow.payment.response.BaseResponse;
import com.sharescrow.payment.response.DataListResponse;
import com.sharescrow.payment.response.DataResponse;
import com.sharescrow.payment.service.HistoryService;
import com.sharescrow.payment.service.OrderService;
import com.sharescrow.payment.service.TransactionService;
import com.sharescrow.payment.service.apiService.ProductApiService;
import com.sharescrow.payment.service.pay.KakaoPayServiceImpl;
import com.sharescrow.payment.service.pay.NaverPayServiceImpl;
import com.sharescrow.payment.service.pay.PayServiceFactory;
import com.sharescrow.payment.service.pay.TestPayServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
	@Autowired
	KakaoPayServiceImpl kakaoPayService;
	@Autowired
	NaverPayServiceImpl naverPayService;
	@Autowired
	TransactionService transactionService;

	// get User's Order List
	@GetMapping("/users")
	public ResponseEntity<BaseResponse> list(@RequestParam("userId") int userId) {
		return new ResponseEntity<>(new DataListResponse(orderService.getOrdersByUserId(userId)), HttpStatus.OK);
	}

	// do order
	@PostMapping("/execute")
	public ResponseEntity<BaseResponse> pay(@RequestParam("payType") String payType,
		@RequestBody Order order) {
		return new ResponseEntity<>(new DataResponse<>(payServiceFactory.getPay(payType).execute(order)),
			HttpStatus.CREATED);
	}

	// transaction callback from naver pay, kakao pay
	@PostMapping("/transaction/done/KAKAO_PAY/{transactionId}")
	public ResponseEntity<BaseResponse> approveKakaoPay(@PathVariable("transactionId") String transactionId,
		@RequestParam("pg_token")String pg_token) {
		return new ResponseEntity<>(new DataResponse<>(kakaoPayService.approve(transactionId,pg_token))
			, HttpStatus.OK);
	}

	@PostMapping("/transaction/done/NAVER_PAY/{transactionId}")
	public ResponseEntity<BaseResponse> approveNaverPay(@PathVariable("transactionId") String transactionId,
		@RequestParam("resultCode")String resultCode, @RequestParam("paymentId")String paymentId) {
		return new ResponseEntity<>(new DataResponse<>(naverPayService.approve(transactionId, resultCode, paymentId))
			, HttpStatus.OK);
	}

	// order cancel, refund
	@GetMapping("/cancel")
	public ResponseEntity<BaseResponse> cancel(@RequestParam("orderId")int orderId){
		Order order = orderService.getOrderById(orderId);
		Transaction transaction = transactionService.getTransactionById(order.getTransactionId());
		return new ResponseEntity<>(new DataResponse<>(payServiceFactory.getPay(transaction.getPlatform()).cancel(order, transaction)), HttpStatus.OK);
	}

	// order confirm
	@GetMapping("/confirm")
	public ResponseEntity<BaseResponse> confirmOrder(@RequestParam("orderId") int orderId) {
		Order order = orderService.getOrderById(orderId);
		historyService.saveHistory(order, HistoryStage.ORDER_CONFIRM);
		return new ResponseEntity<>(new BaseResponse(), HttpStatus.OK);
	}

	//------------------------------------- for test ( because we can't actually do payment) -------------------------//
	@PostMapping("/execute/test")
	public ResponseEntity<BaseResponse> payTest(@RequestBody Order order) {
		return new ResponseEntity<>(new DataResponse(testPayService.execute(order))
		, HttpStatus.OK);
	}

	@PostMapping("/test")
	public ResponseEntity<BaseResponse> test() throws InterruptedException {
		final BaseResponse response = new BaseResponse();
		Thread.sleep(2000);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/test2")
	public ResponseEntity<BaseResponse> test2() {
		productService.cancelOrder(new Order());
		BaseResponse response = new BaseResponse();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
