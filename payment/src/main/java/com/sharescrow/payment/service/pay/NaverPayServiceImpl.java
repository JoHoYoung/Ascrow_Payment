package com.sharescrow.payment.service.pay;

import com.sharescrow.payment.ErrorCode;
import com.sharescrow.payment.context.pay.request.kakao.KakaoPayApiCancelRequest;
import com.sharescrow.payment.context.pay.request.naver.NaverPayApiApproveRequest;
import com.sharescrow.payment.context.pay.request.naver.NaverPayApiCancelRequest;
import com.sharescrow.payment.context.pay.request.naver.NaverPayApiReadyRequest;
import com.sharescrow.payment.context.pay.response.kakao.KakaoPayApiCancelResponse;
import com.sharescrow.payment.context.pay.response.naver.NaverPayApiApproveResponse;
import com.sharescrow.payment.context.pay.response.naver.NaverPayApiCancelResponse;
import com.sharescrow.payment.context.pay.response.naver.NaverPayApiReadyResponse;
import com.sharescrow.payment.context.product.response.ProductValidResponse;
import com.sharescrow.payment.exception.*;
import com.sharescrow.payment.model.Order;
import com.sharescrow.payment.model.Transaction;
import com.sharescrow.payment.response.BaseResponse;
import com.sharescrow.payment.response.DataResponse;
import com.sharescrow.payment.service.HistoryService;
import com.sharescrow.payment.service.OrderService;
import com.sharescrow.payment.service.TransactionService;
import com.sharescrow.payment.service.apiService.NaverPayApiService;
import com.sharescrow.payment.service.apiService.ProductApiService;
import com.sharescrow.payment.service.apiService.QueueApiService;
import com.sharescrow.payment.service.apiService.uri.NaverPayURI;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Service
public class NaverPayServiceImpl implements PayService {

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

	// before start naver pay, save transaction to db
	// in ready status of naver pay, not use network(dont call api), process of set internal call back uri
	// from naver pay after ready
	public DataResponse execute(String params) {
		try {
			// JsonProcessingException Point
			Order order = objectMapper.readValue(params, Order.class);
			// ProductInvalidException Point
			ProductValidResponse productValidResponse = productApiService.isValidOrder(order);

			Transaction transaction = new Transaction();
			transaction.setPlatform("NAVER_PAY");
			transaction.setTransactionAmount(productValidResponse.getTransAmount());
			transaction.setTemporaryState();

			// set callback url not use network
			NaverPayApiReadyRequest naverPayApiReadyRequest = NaverPayApiReadyRequest.builder().returnUrl(
				NaverPayURI.CALL_BACK.getBaseUri()
					.concat(NaverPayURI.CALL_BACK.getEndPoint())
					.concat(transaction.getId())).build();

			// no api call, conceptual
			NaverPayApiReadyResponse naverPayApiReadyResponse = naverPayApiService.ready(naverPayApiReadyRequest);

			order.setTransactionId(transaction.getId());
			order.setCreated();
			// save to db
			transactionService.creatTransaction(transaction);
			orderService.createOrder(order);
			historyService.transactionStart(order);

			return new DataResponse(200, "success", naverPayApiReadyResponse);
		} catch (JsonProcessingException e) {
			throw new InvalidOrderRequestException(ErrorCode.INVALID_ORDER_DATA_FORMAT);
		}
	}

	// naver pay call this function
	// after get call, call naverpay approve uril
	// both Cancel and Fail call /api/v1/order/transaction/done/{payType}/{transactionId}
	// handle if code is Cancel or Fail
	// first and last call to naver pay, other process in client side(conceptually)
	public BaseResponse approve(String transactionId, Map<String, String> params) {
		try {
			// EmptyDataException Point
			Order order = orderService.getOrderByTransactionId(transactionId);
			Transaction transaction = transactionService.getTransactionById(transactionId);

			String resultCode = params.get("resultCode");
			String paymentId = params.get("paymentId");

			if (Objects.isNull(resultCode) || Objects.isNull(paymentId)) {
				throw new UnSupportedOperationException(ErrorCode.UNSUPPORTED_OPERATION);
			}
			try {
				if (resultCode != "Success") {
					throw new TransactionFailException(ErrorCode.FAIL_PAYMENT_TRANSACTION);
				}
				// first resttemplate call
				NaverPayApiApproveRequest naverPayApproveRequest = new NaverPayApiApproveRequest();
				naverPayApproveRequest.setPaymentId(params.get(paymentId));

				// TransactionFailException Point
				NaverPayApiApproveResponse naverPayApiApproveResponse = naverPayApiService.approve(
					naverPayApproveRequest);
				// save snapshot

				transaction.setTransactionKey(paymentId);
				transaction.setDoneState();

				historyService.transactionDone(order);
				transactionService.update(transactionId, transaction);
				// Async enqueue
				queueApiService.enqueue(order);
				return new DataResponse<>(200, "success", naverPayApiApproveResponse);
				// in naverpayapiservice, not 2xx or 2xx But,
				// before approve api call, resultCode at callback not success
				// after approve api call, code is not success
			} catch (TransactionFailException e) {
				// call product for cancel
				// Async Call
				productApiService.cancelOrder(order);
				logger.error("Naver Pay Approve Error order Id : ".concat(Integer.toString(order.getId())));
				throw new TransactionFailException(ErrorCode.FAIL_PAYMENT_TRANSACTION);
				//  if transaction not exist
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
				NaverPayApiCancelRequest naverPayApiCancelRequest = NaverPayApiCancelRequest.builder()
					.cancelAmount(transaction.getTransactionAmount())
					.paymentId(transaction.getTransactionKey()).build();
				NaverPayApiCancelResponse naverPayApiCancelResponse = naverPayApiService.cancel(naverPayApiCancelRequest);

				historyService.transactionCancelDone(order);
				transaction.setDeleted();
				order.setDeleted();
				transactionService.update(transaction.getId(), transaction);
				orderService.updateOrder(order);
				return new DataResponse(200,"success", naverPayApiCancelResponse);
			}catch (TransactionCancelFailException e){
				logger.error("Naver Pay Transaction Cancel Error order Id : ".concat(Integer.toString(order.getId())));
				throw new  TransactionCancelFailException(ErrorCode.TRANASACTION_CANCEL_FAIL);
			}
		}catch (JsonProcessingException e) {
			throw new TransactionCancelFailException(ErrorCode.TRANASACTION_CANCEL_FAIL);
		}
	}

}
