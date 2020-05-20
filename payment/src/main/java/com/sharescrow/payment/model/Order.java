package com.sharescrow.payment.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
	private int id;
	private int minShare;
	private int maxShare;
	private int quantitiy;
	private int transAmount;
	private int productId;
	private int productOptionId;
	private int userId;
	private int groupId;
	private String transactionId;
	private String state;
	@Builder.Default
	private Date createdAt = new Date();
	@Builder.Default
	private Date updatedAt = new Date();

	public void setCreated(){
		this.state = "C";
	}
	public void setDeleted(){
		this.state = "D";
	}
	public void setMatched(){
		this.state = "M";
	}
	public void setExpired(){
		this.state = "E";
	}

	//state C : created, D : deleted, M : matched ,E : expired

}
