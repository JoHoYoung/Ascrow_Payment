package com.sharescrow.payment.service.apiService;

import com.sharescrow.payment.exception.ErrorCode;
import com.sharescrow.payment.context.HistoryStage;
import com.sharescrow.payment.context.product.request.ProductCancelRequest;
import com.sharescrow.payment.context.product.request.ProductValidRequest;
import com.sharescrow.payment.context.product.response.ProductCancelResponse;
import com.sharescrow.payment.context.product.response.ProductValidResponse;
import com.sharescrow.payment.exception.BusinessException;
import com.sharescrow.payment.model.DataState;
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
			return productApiRestTemplate.postForObject(ProductURI.VALID.getEndPoint(), ProductValidRequest.builder()
					.userId(order.getUserId())
					.productOptionId(order.getProductOptionId())
					.quantity(order.getQuantitiy())
					.minShare(order.getMinShare()).build(),
				ProductValidResponse.class);
		} catch (HttpServerErrorException e) {
			logger.error("Product valid api error msg : " + e.getMessage());
			throw new BusinessException(ErrorCode.PRODUCT_INVALID);
		} catch (HttpClientErrorException e) {
			logger.error("Product valid api error msg : " + e.getMessage());
			throw new BusinessException(ErrorCode.PRODUCT_INVALID);
		} catch(Exception e){
			logger.error("Product valid api error msg : " + e.getMessage());
			throw new BusinessException(ErrorCode.PRODUCT_INVALID);
		}
	}

	// Async call because client side No need, no need to know this process.
	@Async("asyncExecutor")
	public void cancelOrder(Order order) {
		try {
			// save snapshot "CANCEL_PENDING"
			historyService.saveHistory(order, HistoryStage.ORDER_CANCEL_PENDING);

			productApiRestTemplate
				.postForObject(ProductURI.CANCEL.getEndPoint(),
					ProductCancelRequest.builder().productId(order.getProductId())
						.quantity(order.getQuantitiy()).build()
					, ProductCancelResponse.class);

			// save snapshot "CANCEL_DONE"
			order.setState(DataState.DELETED);
			orderService.updateOrder(order);
			historyService.saveHistory(order, HistoryStage.ORDER_CANCEL_DONE);
			// #Todo : if product cancel fails, save to message queue or do batch job for retry(conceptual)
		} catch (HttpServerErrorException e) {
			logger.error("Product cancel api error msg : " + e.getMessage());
		} catch (HttpClientErrorException e) {
			logger.error("Product cancel api error msg : " + e.getMessage());
		} catch (Exception e){
			logger.error("Product cancel api error msg : " + e.getMessage());
		}
	}

}
