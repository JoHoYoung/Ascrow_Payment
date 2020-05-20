package com.sharescrow.payment.service;

import java.util.Objects;

import com.sharescrow.payment.ErrorCode;
import com.sharescrow.payment.exception.EmptyDataException;
import com.sharescrow.payment.exception.UnSupportedOperationException;
import com.sharescrow.payment.exception.UnauthorizedAccessException;
import com.sharescrow.payment.model.History;
import com.sharescrow.payment.model.Order;
import com.sharescrow.payment.repository.HistoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HistoryService {

	@Autowired
	HistoryRepository historyRepository;

	public enum Stage {
		ORDER_START("ORDER_START", "when user come in and transcation start(not done)"),
		// call transactio api,
		TRANSACTION_PENDING("TRANSACTION_PENDING",
			"call transcation ready api, but not execute approve (in kakao, naverpay"),
		TRANSACTION_CANCEL_DONE("TRNSACTION_CANCEL_DONE", "Call transaction's api cancel"),
		TRANSACTION_CANCEL_PENDING("TRANSACTION_CANCEL_PENDING", "call transaction's cancel api but not OK"),
		TRANSACTION_DONE("TRANSACTION_DONE", "call transaction api, and OK"),
		// call product's cancel apu
		ORDER_CANCEL_PENDING("ORDER_CANCEL_PENDING", "call product's cancel api but not confirmed yet"),
		ORDER_CANCEL_DONE("ORDER_CANCEL_DONE", "call product's cancel api and get OK"),
		GET_GOODS_PENDING("GET_GOODS_PENDING", "trasnaction done but not matched yet"),
		// group is generated
		GET_GOODS("GET_GOODS", "user get products and start using (like matching to group in this service"),
		// last stage
		ORDER_CONFIRM("ORDER_CONFIRM", "an order is confirmed by user(or automatically and platfrom get money");

		private String stage;
		private String descripion;

		Stage(String stage, String description) {
			this.stage = stage;
			this.descripion = description;
		}

		public String getStage() {
			return this.stage;
		}
		public String getDescripion() {
			return this.descripion;
		}
	}

	public int getConfirmedNumber(int groupId){
		return historyRepository.getConfirmedNumber(groupId);
	}

	public History getLastestHistoryByOrderId(int orderId){
		History history = historyRepository.getLatestHistoryByOrderId(orderId);
		if(Objects.isNull(history)){
			throw new EmptyDataException(ErrorCode.EMPTY_DATA_SET);
		}
		return history;
	}

	public void orderStart(Order order) {
		this.historyRepository.insert(History.builder()
			.orderId(order.getId())
			.stage(Stage.ORDER_START.getStage()).build());
	}

	public void orderConfirm(Order order) {
		History history = this.getLastestHistoryByOrderId(order.getId());
		// user can confirm it only after you receive the product.
		// if not, raise error
		if(history.getStage() != Stage.GET_GOODS.getStage()){
			throw new UnSupportedOperationException(ErrorCode.UNSUPPORTED_OPERATION);
		}
		this.historyRepository.insert(History.builder()
			.orderId(order.getId())
			.stage(Stage.ORDER_CONFIRM.getStage()).build());
	}

	public void orderCancelStart(Order order) {
		this.historyRepository.insert(History.builder()
			.orderId(order.getId())
			.stage(Stage.ORDER_CANCEL_PENDING.getStage()).build());
	}

	public void orderCancelDone(Order order) {
		this.historyRepository.insert(History.builder()
			.orderId(order.getId())
			.stage(Stage.ORDER_CANCEL_DONE.getStage()).build());
	}

	public void transactionStart(Order order) {
		this.historyRepository.insert(History.builder()
			.orderId(order.getId())
			.stage(Stage.TRANSACTION_PENDING.getStage()).build());
	}

	public void transactionDone(Order order) {
		this.historyRepository.insert(History.builder()
			.orderId(order.getId())
			.stage(Stage.TRANSACTION_DONE.getStage()).build());

	}

	public void getGoodsPending(Order order){
		this.historyRepository.insert(History.builder()
			.orderId(order.getId())
			.stage(Stage.GET_GOODS_PENDING.getStage()).build());
	}

	public void getGoods(Order order) {
		this.historyRepository.insert(History.builder()
			.orderId(order.getId())
			.stage(Stage.GET_GOODS.getStage()).build());
	}

	public void transactionCancelStart(Order order) {
		this.historyRepository.insert(History.builder()
			.orderId(order.getId())
			.stage(Stage.TRANSACTION_CANCEL_PENDING.getStage()).build());
	}

	public void transactionCancelDone(Order order) {
		this.historyRepository.insert(History.builder()
			.orderId(order.getId())
			.stage(Stage.TRANSACTION_CANCEL_DONE.getStage()).build());
	}
}
