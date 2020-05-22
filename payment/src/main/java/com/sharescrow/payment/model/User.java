package com.sharescrow.payment.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
	private Integer id;
	private String name;
	@Setter
	private DataState state;
	@Builder.Default
	private Date createdAt = new Date();
	@Builder.Default
	private Date updatedAt = new Date();
}
