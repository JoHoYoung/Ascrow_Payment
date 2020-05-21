package com.sharescrow.payment.service.pay;

import com.sharescrow.payment.ErrorCode;
import com.sharescrow.payment.context.pay.request.naver.NaverPayApiCancelRequest;
import com.sharescrow.payment.context.pay.request.openbank.OpenBankApiCancelRequest;
import com.sharescrow.payment.context.pay.request.openbank.OpenBankApiRequest;
import com.sharescrow.payment.context.pay.response.naver.NaverPayApiCancelResponse;
import com.sharescrow.payment.context.pay.response.openbank.OpenBankApiCancelResponse;
import com.sharescrow.payment.context.pay.response.openbank.OpenBankApiResponse;
import com.sharescrow.payment.context.product.response.ProductValidResponse;
import com.sharescrow.payment.exception.InvalidOrderRequestException;
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
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OpenbankPayServiceImpl implements PayService {

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

	public DataResponse execute(String params) {
		try {
			// JsonProcessingException Point
			Order order = objectMapper.readValue(params, Order.class);
			try {
				ProductValidResponse productValidResponse = productApiService.isValidOrder(order);

				JsonObject jsonObject = new JsonParser().parse(params).getAsJsonObject();

				OpenBankApiRequest openBankApiRequest = objectMapper.readValue(jsonObject.get("transactionInfo").toString(),
					OpenBankApiRequest.class);

				// TransactionFailException Point
				OpenBankApiResponse openBankApiResponse = openBankAPIService.execute(openBankApiRequest);
				historyService.transactionDone(order);

				Transaction transaction = Transaction.builder()
					.platform("OPEN_BANK")
					.transactionKey(openBankApiResponse.getApi_tran_id())
					.transactionAmount(productValidResponse.getTransAmount()).build();

				transaction.setDoneState();

				order.setTransactionId(transaction.getId());
				order.setCreated();

				// save snapshot
				orderService.createOrder(order);
				transactionService.creatTransaction(transaction);
				historyService.transactionDone(order);
				// Async enqueue
				queueApiService.enqueue(order);
				return new DataResponse(200, "success", openBankApiResponse);
			} catch (TransactionFailException e) {
				// call product for cancel
				// Async Call
				productApiService.cancelOrder(order);
				throw new TransactionFailException(ErrorCode.FAIL_PAYMENT_TRANSACTION);
			}
		} catch (JsonProcessingException e) {
			throw new InvalidOrderRequestException(ErrorCode.INVALID_ORDER_DATA_FORMAT);
		}
	}

	public BaseResponse approve(String transactionId, Map<String, String> params) {
		throw new UnSupportedOperationException(ErrorCode.UNSUPPORTED_OPERATION);
	}

	public DataResponse cancel(String params){
		try{
			Order order = objectMapper.readValue(params, Order.class);
			Transaction transaction = transactionService.getTransactionById(order.getTransactionId());
			try{
				JsonObject jsonObject = new JsonParser().parse(params).getAsJsonObject();
				historyService.transactionCancelStart(order);
				OpenBankApiCancelRequest openBankApiCancelRequest = objectMapper.readValue(jsonObject.get("transactionInfo").toString(),
					OpenBankApiCancelRequest.class);

				OpenBankApiCancelResponse openBankApiCancelResponse = openBankAPIService.cancel(openBankApiCancelRequest);


				historyService.transactionCancelDone(order);
				transaction.setDeleted();
				order.setDeleted();
				transactionService.update(transaction.getId(), transaction);
				orderService.updateOrder(order);
				return new DataResponse(200,"success", openBankApiCancelResponse);

			}catch (TransactionCancelFailException e){
				logger.error("OpenBank Transaction Cancel Error order Id : ".concat(Integer.toString(order.getId())));
				throw new  TransactionCancelFailException(ErrorCode.TRANASACTION_CANCEL_FAIL);
			}
		}catch (JsonProcessingException e) {
			throw new TransactionCancelFailException(ErrorCode.TRANASACTION_CANCEL_FAIL);
		}
	}
}
