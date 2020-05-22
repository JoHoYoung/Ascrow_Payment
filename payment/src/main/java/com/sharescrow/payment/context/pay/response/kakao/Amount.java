package com.sharescrow.payment.context.pay.response.kakao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Amount {
	private Integer total; // 3200,
	private Integer tax_free; // 0,
	private Integer vat; // 0,
	private Integer discount; // 0,
	private Integer poInteger; // 0
}
