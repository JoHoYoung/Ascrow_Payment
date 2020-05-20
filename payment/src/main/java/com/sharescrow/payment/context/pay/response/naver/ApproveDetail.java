package com.sharescrow.payment.context.pay.response.naver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApproveDetail {
	private String productName; // 상품명
	private String merchantId; // loginId
	private String merchantName; // 가맹점명
	private String cardNo; // 465887**********
	private String admissionYmdt; // 20170201151722
	private String payHistId; // 20170201NP1043587781
	private String primaryPayAmount; // 1000,
	private String npointPayAmount; // 0,
	private String totalPayAmount; // 1000,
	private String primaryPayMeans; // CARD
	private String merchantPayKey; // order-key
	private String merchantUserKey; // jenie
	private String cardCorpCode; // C0
	private String paymentId; // 20170201NP1043587746
	private String admissionTypeCode; // 01
	private String settleExpectAmount; // 971,
	private String payCommissionAmount; // 29,
	private String admissionState; // SUCCESS
	private String tradeConfirmYmdt; // 20170201152510
	private String cardAuthNo; // 17545616
	private String cardInstCount; // 0,
	private String bankCorpCode; // ; //
	private String bankAccountNo; // ; //
	private String settleExpected; // false
	private String extraDeduction; // false
	private String useCfmYmdt; // 20180703
}
