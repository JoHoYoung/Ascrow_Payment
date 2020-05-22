package com.sharescrow.payment.context.pay.response.naver;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CancelBody {
	private String paymentId;    //String	50바이트	필수	네이버페이 결제번호
	private String payHistId;    // String	50바이트	필수	취소 결제 번호
	private String primaryPayMeans; //String	10바이트	필수	취소 처리된 주 결제 수단(CARD: 신용카드, BANK: 계좌 이체)
	private Integer primaryPayCancelAmount;//	Number		필수	주 결제 수단 취소 금액
	private Integer primaryPayRestAmount;//	Number		필수	추가로 취소 가능한 주 결제 수단 잔여 결제 금액
	private Integer npoIntegerCancelAmount;//	Number		필수	네이버페이 포인트 취소 금액
	private Integer npoIntegerRestAmount;//	Number		필수	추가로 취소 가능한 네이버페이 포인트 잔여 결제 금액
	private String cancelYmdt;//	String	14바이트	필수	취소 일시(YYYYMMDDHH24MMSS)
	private Integer totalRestAmount;//	Number		필수	추가로 취소 가능한 전체 잔여 결제 금액(primaryPayRestAmount + npoIntegerRestAmount)
}
