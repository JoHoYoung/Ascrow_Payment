package com.sharescrow.payment.context.pay.response.kakao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Amount {
	private int total; // 3200,
	private int tax_free; // 0,
	private int vat; // 0,
	private int discount; // 0,
	private int point; // 0
}
