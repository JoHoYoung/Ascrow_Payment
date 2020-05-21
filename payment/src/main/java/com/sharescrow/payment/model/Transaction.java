package com.sharescrow.payment.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
	private int transactionAmount;
	private String state;
	@Builder.Default
	private Date createdAt = new Date();
	@Builder.Default
	private Date updatedAt = new Date();

	// state T : temporal, C : created, D :deleted
	public void setTemporaryState(){
		this.state = "T";
	}
	public void setDoneState(){
		this.state = "C";
	}
	public void setDeleted(){
		this.state = "D";
	}

}
