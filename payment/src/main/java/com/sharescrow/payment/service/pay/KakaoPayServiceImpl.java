package com.sharescrow.payment.service.pay;

import com.sharescrow.payment.ErrorCode;
import com.sharescrow.payment.context.pay.request.kakao.KakaoPayApiApproveRequest;
import com.sharescrow.payment.context.pay.request.kakao.KakaoPayApiCancelRequest;
import com.sharescrow.payment.context.pay.request.kakao.KakaoPayApiReadyRequest;
import com.sharescrow.payment.context.pay.response.kakao.KakaoPayApiApproveCardResponse;
import com.sharescrow.payment.context.pay.response.kakao.KakaoPayApiCancelResponse;
import com.sharescrow.payment.context.pay.response.kakao.KakaoPayApiReadyResponse;
import com.sharescrow.payment.context.product.response.ProductValidResponse;
import com.sharescrow.payment.exception.EmptyDataException;
import com.sharescrow.payment.exception.InvalidOrderRequestException;
import com.sharescrow.payment.exception.PayReadyException;
import com.sharescrow.payment.exception.TransactionCancelFailException;
import com.sharescrow.payment.exception.TransactionFailException;
import com.sharescrow.payment.exception.UnSupportedOperationException;
import com.sharescrow.payment.model.Order;
import com.sharescrow.payment.model.Transaction;
import com.sharescrow.payment.response.BaseResponse;
import com.sharescrow.payment.response.DataResponse;
import com.sharescrow.payment.service.HistoryService;
import com.sharescrow.payment.service.OrderService;
import com.sharescrow.payment.service.TransactionService;
import com.sharescrow.payment.service.apiService.KakaoPayApiService;
import com.sharescrow.payment.service.apiService.ProductApiService;
import com.sharescrow.payment.service.apiService.QueueApiService;
import com.sharescrow.payment.service.apiService.uri.KakaoPayURI;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Service
public class KakaoPayServiceImpl implements PayService {

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

	// call kakao pay ready api
	// 1. product valid call
	// 2. kakao pay ready call
	// if 1 fails, its error dealt at productApiService Class and nothing to do
	// but if 2 fails, must call product's cancel api for recovering stock
	public DataResponse execute(String params) {
		try {
			// JsonProcessingException Point
			Order order = objectMapper.readValue(params, Order.class);
			// is valid order?
			try {
				// ProductInvalidException Point
				ProductValidResponse productValidResponse = productApiService.isValidOrder(order);
				Transaction transaction = new Transaction();
				// save snapshot

				KakaoPayApiReadyRequest kakaoPayApiReadyRequest = KakaoPayApiReadyRequest.builder()
					.item_name(productValidResponse.getProductName())
					.total_amount(productValidResponse.getTransAmount())
					.quantity(order.getQuantitiy())
					.approval_url(KakaoPayURI.DONE_CALLBACK.getBaseUri()
						.concat(KakaoPayURI.DONE_CALLBACK.getEndPoint())
						.concat(transaction.getId()))
					.cancel_url(KakaoPayURI.CANCEL_CALLBACK.getBaseUri()
						.concat(KakaoPayURI.DONE_CALLBACK.getEndPoint())
						.concat(transaction.getId()))
					.fail_url(KakaoPayURI.FAIL_CALLBACK.getBaseUri()
						.concat(KakaoPayURI.DONE_CALLBACK.getEndPoint())
						.concat(transaction.getId())).build();

				// PayReadyException Point
				KakaoPayApiReadyResponse kakaoPayApiReadyResponse = kakaoPayApiService.ready(kakaoPayApiReadyRequest);
				// if OK
				transaction.setPlatform("KAKAO_PAY");
				transaction.setTransactionKey(kakaoPayApiReadyResponse.getTid());
				transaction.setTransactionAmount(productValidResponse.getTransAmount());
				transaction.setTemporaryState();

				order.setTransactionId(transaction.getId());
				order.setCreated();
				// save data

				transactionService.creatTransaction(transaction);
				orderService.createOrder(order);
				historyService.transactionStart(order);

				return new DataResponse(200, "sucess", kakaoPayApiReadyResponse);
			} catch (PayReadyException e) {
				// call product for cancel
				// Async Call
				logger.error("Kakao Pay Ready Error order Id : ".concat(Integer.toString(order.getId())));
				productApiService.cancelOrder(order);
				throw new TransactionFailException(ErrorCode.FAIL_PAYMENT_TRANSACTION);
				//  if transaction not exist
			}
		} catch (JsonProcessingException e) {
			throw new InvalidOrderRequestException(ErrorCode.INVALID_ORDER_DATA_FORMAT);
		}
	}

	// call kakao pay approve api
	// 1. if success, enque and save snapshot
	// 2. if fails,
	public BaseResponse approve(String transactionId, Map<String, String> params) {
		try {
			if (Objects.isNull(params.get("pg_token"))) {
				throw new UnSupportedOperationException(ErrorCode.UNSUPPORTED_OPERATION);
			}
			// EmptyDataException Point
			Order order = orderService.getOrderByTransactionId(transactionId);
			Transaction transaction = transactionService.getTransactionById(transactionId);
			try {
				KakaoPayApiApproveRequest kakaoPayApiApproveRequest = KakaoPayApiApproveRequest.builder()
					.tid(transaction.getTransactionKey())
					.pg_token(params.get("pg_token")).build();

				// TransactionFailException Point
				KakaoPayApiApproveCardResponse kakaoPayApiApproveCardResponse = kakaoPayApiService.approve(
					kakaoPayApiApproveRequest);
				transaction.setDoneState();
				// save snapshot
				historyService.transactionDone(order);
				transactionService.update(transactionId, transaction);
				// Async enqueue
				queueApiService.enqueue(order);
				return new DataResponse(200, "success", kakaoPayApiApproveCardResponse);
			} catch (TransactionFailException e) {
				// Async Call
				logger.error("Kakao Pay Approve Error order Id : ".concat(Integer.toString(order.getId())));
				productApiService.cancelOrder(order);
				throw new TransactionFailException(ErrorCode.FAIL_PAYMENT_TRANSACTION);
				// if transaction not exist
			}
		} catch (EmptyDataException e) {
			throw new UnSupportedOperationException(ErrorCode.UNSUPPORTED_OPERATION);
		}
	}

	public DataResponse cancel(String params){
		try{
			Order order = objectMapper.readValue(params, Order.class);
			Transaction transaction = transactionService.getTransactionById(order.getTransactionId());
			try{

				historyService.transactionCancelStart(order);
				KakaoPayApiCancelRequest kakaoPayApiCancelRequest = KakaoPayApiCancelRequest.builder()
					.cancel_amount(transaction.getTransactionAmount())
					.tid(transaction.getTransactionKey()).build();
				KakaoPayApiCancelResponse kakaoPayApiCancelResponse = kakaoPayApiService.cancel(kakaoPayApiCancelRequest);

				historyService.transactionCancelDone(order);

				transaction.setDeleted();
				order.setDeleted();
				transactionService.update(transaction.getId(), transaction);
				orderService.updateOrder(order);
				return new DataResponse(200,"success", kakaoPayApiCancelResponse);
			}catch (TransactionCancelFailException e){
				logger.error("Kakao Pay Transaction Cancel Error order Id : ".concat(Integer.toString(order.getId())));
				throw new  TransactionCancelFailException(ErrorCode.TRANASACTION_CANCEL_FAIL);
			}
		}catch (JsonProcessingException e) {
			throw new TransactionCancelFailException(ErrorCode.TRANASACTION_CANCEL_FAIL);
		}
	}

}