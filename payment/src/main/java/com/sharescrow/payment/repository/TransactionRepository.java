package com.sharescrow.payment.repository;

import com.sharescrow.payment.model.Transaction;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository {

	Transaction selectById(@Param("id") String id);

	void insert(Transaction transaction);

	void update(@Param("id") String id, @Param("transaction") Transaction transaction);
}
