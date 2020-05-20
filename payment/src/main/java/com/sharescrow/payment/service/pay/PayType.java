package com.sharescrow.payment.service.pay;

import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

public enum PayType {

	NAVER_PAY("naver pay", "pay with naver pay", "P001"),
	KAKAO_PAY("kakao pay", "pay with kakao pay", "P002"),
	OPEN_BANK("open bank", "pay with open bank", "P003");

	private String name;
	private String description;
	private String code;

	@Setter
	@Getter
	private PayService payService;

	@Component
	private static class PayServiceInjector {
		@Autowired
		NaverPayServiceImpl naverPayService;
		@Autowired
		KakaoPayServiceImpl kakaoPayService;
		@Autowired
		OpenbankPayServiceImpl openbankPayService;

		@PostConstruct
		public void postConstruct() {
			PayType.valueOf("NAVER_PAY").setPayService(naverPayService);
			PayType.valueOf("KAKAO_PAY").setPayService(kakaoPayService);
			PayType.valueOf("OPEN_BANK").setPayService(openbankPayService);
		}
	}

	PayType(final String name, final String description, final String code) {
		this.name = name;
		this.description = description;
		this.code = code;
	}

}
