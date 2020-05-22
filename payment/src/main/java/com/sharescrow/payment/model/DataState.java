package com.sharescrow.payment.model;

public enum DataState {

	CREATED("data created"),
	TEMPORARY("temporal data"),
	DELETED("deleted data"),
	MATCHED("group matched order"),
	EXPIRED("group expired");

	private final String description;

	DataState(final String description){
		this.description = description;
	}

}
