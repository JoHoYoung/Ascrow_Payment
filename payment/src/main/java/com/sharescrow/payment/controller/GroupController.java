package com.sharescrow.payment.controller;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sharescrow.payment.ErrorCode;
import com.sharescrow.payment.context.group.request.GroupMatchedRequest;
import com.sharescrow.payment.exception.InvalidParameterException;
import com.sharescrow.payment.model.Order;
import com.sharescrow.payment.response.BaseResponse;
import com.sharescrow.payment.service.HistoryService;
import com.sharescrow.payment.service.OrderService;

@RestController
@RequestMapping("/api/v1/group")
public class GroupController {

	@Autowired
	OrderService orderService;
	@Autowired
	HistoryService historyService;

	@PostMapping("/matched")
	public ResponseEntity<BaseResponse> groupMatched(@RequestBody GroupMatchedRequest groupMatchedRequest){

		if(Objects.isNull(groupMatchedRequest.getGroupId()) || groupMatchedRequest.getOrderIdList().isEmpty()){
			throw new InvalidParameterException(ErrorCode.INVALID_REQUEST_PARAM);
		}

		List<Integer> orderIdList = groupMatchedRequest.getOrderIdList();
		for(int orderId : orderIdList){
			// set group id to order scheme
			// save history GET_GOODS_PENDING -> GET_GOODS
			Order order = orderService.getOrderById(orderId);
			order.setGroupId(groupMatchedRequest.getGroupId());
			order.setMatched();
			orderService.updateOrder(order);
			historyService.getGoods(order);
		}
		return new ResponseEntity<>(new BaseResponse(200,"success"), HttpStatus.OK);
	}

	@GetMapping("/expired")
	public ResponseEntity<BaseResponse> groupExpired(@RequestParam("groupId")int groupId){
		List<Order> orderList = orderService.getOrdersByGroupId(groupId);
		for(Order order : orderList){
			order.setExpired();
			orderService.updateOrder(order);
		}
		return new ResponseEntity<>(new BaseResponse(200,"success"), HttpStatus.OK);
	}

}
