package com.sharescrow.payment.repository;

import com.sharescrow.payment.model.History;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepository {
	void insert(History history);

	int getConfirmedNumber(@Param("groupId")int groupId);

	History getLatestHistoryByOrderId(@Param("orderId")int orderId);

}
