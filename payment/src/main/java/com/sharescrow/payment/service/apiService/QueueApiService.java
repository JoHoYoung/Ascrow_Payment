package com.sharescrow.payment.service.apiService;

import com.sharescrow.payment.context.HistoryStage;
import com.sharescrow.payment.context.queue.request.EnqueueRequest;
import com.sharescrow.payment.context.queue.response.EnqueueResponse;
import com.sharescrow.payment.model.Order;
import com.sharescrow.payment.service.HistoryService;
import com.sharescrow.payment.service.apiService.uri.QueueURI;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class QueueApiService {

	private final Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	RestTemplate queueAPIRestTemplate;

	@Autowired
	HistoryService historyService;

	// Async call because client side No need, no need to know this process.
	@Async("asyncExecutor")
	public void enqueue(Order order) {
		try {

			queueAPIRestTemplate.postForObject(QueueURI.ENQUEUE.getEndPoint(), EnqueueRequest.builder()
				.maxShare(order.getMaxShare())
				.minShare(order.getMinShare())
				.orderId(order.getId())
				.productId(order.getProductId())
				.userId(order.getUserId()), EnqueueResponse.class);

			historyService.saveHistory(order, HistoryStage.GET_GOODS_PENDING);
			// #Todo : if enque is fails, save to messaging queue for retry(not on this proejct but conceptually..)
		} catch (HttpServerErrorException e) {
			logger.error("Enqueue api error msg : " + e.getMessage());
		} catch (HttpClientErrorException e) {
			logger.error("Enqueue valid api error msg : " + e.getMessage());
		}
	}

}
