package com.sharescrow.payment.model;

import com.sharescrow.payment.context.HistoryStage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class History {
	private Integer id;
	private Integer orderId;
	@Setter
	private HistoryStage stage;
	private DataState state;
	@Builder.Default
	private Date createdAt = new Date();
	@Builder.Default
	private Date updatedAt = new Date();

}
