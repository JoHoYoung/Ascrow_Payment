package com.sharescrow.payment.controller;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sharescrow.payment.exception.ErrorCode;
import com.sharescrow.payment.context.HistoryStage;
import com.sharescrow.payment.context.group.request.GroupMatchedRequest;
import com.sharescrow.payment.exception.BusinessException;
import com.sharescrow.payment.model.DataState;
import com.sharescrow.payment.response.BaseResponse;
import com.sharescrow.payment.service.HistoryService;
import com.sharescrow.payment.service.OrderService;
import com.sharescrow.payment.util.Util;

@RestController
@RequestMapping("/api/v1/group")
public class GroupController {

	@Autowired
	OrderService orderService;
	@Autowired
	HistoryService historyService;

	@PostMapping("/matched")
	@Transactional(rollbackFor = {BusinessException.class})
	public ResponseEntity<BaseResponse> groupMatched(@RequestBody GroupMatchedRequest groupMatchedRequest) {
		if (Objects.isNull(groupMatchedRequest.getGroupId()) || Util.collectionEmpty(groupMatchedRequest.getOrderIdList())) {
			throw new BusinessException(ErrorCode.INVALID_REQUEST_PARAM);
		}
		historyService.saveMultipleHisotry(groupMatchedRequest.getOrderIdList(), HistoryStage.GET_GOODS);
		orderService.updateWhenGroupMatched(groupMatchedRequest.getOrderIdList(), groupMatchedRequest.getGroupId(), DataState.MATCHED);
		return new ResponseEntity<>(new BaseResponse(), HttpStatus.OK);
	}

	@GetMapping("/expired")
	public ResponseEntity<BaseResponse> groupExpired(@RequestParam("groupId") int groupId) {
		orderService.updateWhenGroupExpired(groupId);
		return new ResponseEntity<>(new BaseResponse(), HttpStatus.OK);
	}

}
