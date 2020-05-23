package com.sharescrow.payment.service;

import java.util.List;
import java.util.Objects;

import com.sharescrow.payment.exception.ErrorCode;
import com.sharescrow.payment.context.HistoryStage;
import com.sharescrow.payment.exception.BusinessException;
import com.sharescrow.payment.model.History;
import com.sharescrow.payment.model.Order;
import com.sharescrow.payment.repository.HistoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HistoryService {

	@Autowired
	HistoryRepository historyRepository;


	public int getConfirmedNumber(int groupId){
		return historyRepository.getConfirmedNumber(groupId);
	}

	public History getLastestHistoryByOrderId(int orderId){
		History history = historyRepository.getLatestHistoryByOrderId(orderId);
		if(Objects.isNull(history)){
			throw new BusinessException(ErrorCode.EMPTY_DATA_SET);
		}
		return history;
	}

	public void saveHistory(Order order, HistoryStage historyStage){

		if(historyStage.equals(HistoryStage.ORDER_CONFIRM)){
			History history = this.getLastestHistoryByOrderId(order.getId());
			if(!history.getStage().equals(HistoryStage.GET_GOODS)){
				throw new BusinessException(ErrorCode.UNSUPPORTED_OPERATION);
			}
		}
		this.historyRepository.insert(History.builder().orderId(order.getId())
			.stage(historyStage).build());
	}

	public void saveMultipleHisotry(List<Integer> orderIds, HistoryStage historyStage){
		for(int orderId : orderIds){
			this.historyRepository.insert(History.builder().orderId(orderId)
			.stage(historyStage).build());
		}
	}

}
