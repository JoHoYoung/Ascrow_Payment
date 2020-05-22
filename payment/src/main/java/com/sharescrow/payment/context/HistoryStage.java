package com.sharescrow.payment.context;

import com.sharescrow.payment.service.HistoryService;

public enum HistoryStage {
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

	HistoryStage(String stage, String description) {
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
