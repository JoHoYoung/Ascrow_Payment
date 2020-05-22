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
@NoArgsConstructor
@AllArgsConstructor
public class Order <T>{
	private Integer id;
	private Integer minShare;
	private Integer maxShare;
	private Integer quantitiy;
	private Integer transAmount;
	private Integer productId;
	private Integer productOptionId;
	private Integer userId;
	private Integer groupId;
	private String transactionId;
	private T transaction;
	@Setter
	private DataState state;
	@Builder.Default
	private Date createdAt = new Date();
	@Builder.Default
	private Date updatedAt = new Date();

	//state C : created, D : deleted, M : matched ,E : expired

}
