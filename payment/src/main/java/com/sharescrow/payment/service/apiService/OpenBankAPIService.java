package com.sharescrow.payment.service.apiService;

import com.sharescrow.payment.ErrorCode;
import com.sharescrow.payment.context.HistoryStage;
import com.sharescrow.payment.context.pay.request.openbank.OpenBankApiCancelRequest;
import com.sharescrow.payment.context.pay.request.openbank.OpenBankApiRequest;
import com.sharescrow.payment.context.pay.response.openbank.OpenBankApiCancelResponse;
import com.sharescrow.payment.context.pay.response.openbank.OpenBankApiResponse;
import com.sharescrow.payment.context.product.response.ProductValidResponse;
import com.sharescrow.payment.exception.TransactionCancelFailException;
import com.sharescrow.payment.exception.TransactionFailException;
import com.sharescrow.payment.model.DataState;
import com.sharescrow.payment.model.Order;
import com.sharescrow.payment.model.Transaction;
import com.sharescrow.payment.service.OrderService;
import com.sharescrow.payment.service.TransactionService;
import com.sharescrow.payment.service.apiService.uri.OpenBankURI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Service
// open banking error case :  only http Status Code
public class OpenBankAPIService {

	@Autowired
	RestTemplate openBankAPIRestTemplate;
	@Autowired
	ProductApiService productApiService;
	@Autowired
	TransactionService transactionService;
	@Autowired
	OrderService orderService;

	// error 가능
	public OpenBankApiResponse callExecuteApi(Object openBankApiRequest) throws
		HttpServerErrorException,
		HttpClientErrorException {
		ResponseEntity<OpenBankApiResponse> response = openBankAPIRestTemplate
			.postForEntity(OpenBankURI.APPROVE.getEndPoint(), openBankApiRequest, OpenBankApiResponse.class);
		return response.getBody();

	}

	// error 가능
	public OpenBankApiResponse execute(Order order) {
		try {

			ProductValidResponse productValidResponse = productApiService.isValidOrder(order);
			OpenBankApiResponse openBankApiResponse = this.callExecuteApi(order.getTransaction());

			Transaction transaction = Transaction.builder()
				.platform("OPEN_BANK")
				.transactionKey(openBankApiResponse.getApi_tran_id())
				.transactionAmount(productValidResponse.getTransAmount()).build();
			transaction.setState(DataState.CREATED);
			order.setTransactionId(transaction.getId());
			order.setState(DataState.CREATED);
			// save snapshot
			orderService.createOrder(order);
			transactionService.creatTransaction(transaction);

			return openBankApiResponse;
		} catch (HttpServerErrorException e) {
			throw new TransactionFailException(ErrorCode.FAIL_PAYMENT_TRANSACTION);
		} catch (HttpClientErrorException e) {
			throw new TransactionFailException(ErrorCode.FAIL_PAYMENT_TRANSACTION);
		}
	}

	public OpenBankApiCancelResponse callCancelApi(Object openBankApiCancelRequest) throws
		HttpServerErrorException,
		HttpClientErrorException {
		ResponseEntity<OpenBankApiCancelResponse> response = openBankAPIRestTemplate
			.postForEntity(OpenBankURI.CANCEL.getEndPoint(), openBankApiCancelRequest,
				OpenBankApiCancelResponse.class);
		return response.getBody();
	}

	public OpenBankApiCancelResponse cancel(Order order, Transaction transaction) throws HttpServerErrorException, HttpClientErrorException{
		try {

			OpenBankApiCancelResponse openBankApiCancelResponse = this.callCancelApi(order.getTransaction());
			transaction.setState(DataState.DELETED);
			order.setState(DataState.DELETED);
			transactionService.update(transaction.getId(), transaction);
			orderService.updateOrder(order);
			return openBankApiCancelResponse;
		} catch (HttpServerErrorException e) {
			throw new TransactionCancelFailException(ErrorCode.FAIL_PAYMENT_TRANSACTION);
		} catch (HttpClientErrorException e) {
			throw new TransactionCancelFailException(ErrorCode.FAIL_PAYMENT_TRANSACTION);
		}
	}

}


