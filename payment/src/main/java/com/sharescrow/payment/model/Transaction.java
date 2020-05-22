package com.sharescrow.payment.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
	@Builder.Default
	private String id = UUID.randomUUID().toString();
	private String platform;
	private String transactionKey;
	private Integer transactionAmount;

	@Setter
	@Builder.Default
	private DataState state = DataState.CREATED;
	@Builder.Default
	private Date createdAt = new Date();
	@Builder.Default
	private Date updatedAt = new Date();

}
