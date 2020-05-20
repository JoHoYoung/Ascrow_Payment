package com.sharescrow.payment.service.apiService;

import com.sharescrow.payment.ErrorCode;
import com.sharescrow.payment.context.product.response.ProductCancelResponse;
import com.sharescrow.payment.context.product.response.ProductValidResponse;
import com.sharescrow.payment.exception.ProductInvalidException;
import com.sharescrow.payment.exception.TransactionFailException;
import com.sharescrow.payment.model.Order;
import com.sharescrow.payment.service.HistoryService;
import com.sharescrow.payment.service.OrderService;
import com.sharescrow.payment.service.apiService.uri.ProductURI;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class ProductApiService {

	private final Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	RestTemplate productApiRestTemplate;

	@Autowired
	HistoryService historyService;
	@Autowired
	OrderService orderService;

	public ProductValidResponse isValidOrder(Order order) {
		try {
			return productApiRestTemplate.postForObject(ProductURI.VALID.getEndPoint(), order,
				ProductValidResponse.class);
		} catch (HttpServerErrorException e) {
			logger.error("Product valid api error msg : " + e.getMessage());
			throw new ProductInvalidException(ErrorCode.PRODUCT_INVALID);
		} catch (HttpClientErrorException e) {
			logger.error("Product valid api error msg : " + e.getMessage());
			throw new ProductInvalidException(ErrorCode.PRODUCT_INVALID);
		}
	}

	// Async call because client side No need, no need to know this process.
	@Async("asyncExecutor")
	public void cancelOrder(Order order) {
		try {
			// save snapshot "CANCEL_PENDING"
			historyService.orderCancelStart(order);
			productApiRestTemplate
				.postForObject(ProductURI.CANCEL.getEndPoint(), order, ProductCancelResponse.class);
			// save snapshot "CANCEL_DONE"
			order.setDeleted();
			orderService.updateOrder(order);
			historyService.orderCancelDone(order);
			// #Todo : if product cancel fails, save to message queue or do batch job for retry(conceptual)
		} catch (HttpServerErrorException e) {
			logger.error("Product cancel api error msg : " + e.getMessage());
		} catch (HttpClientErrorException e) {
			logger.error("Product cancel api error msg : " + e.getMessage());
		}
	}

}
