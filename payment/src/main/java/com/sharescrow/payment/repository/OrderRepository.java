package com.sharescrow.payment.repository;

import com.sharescrow.payment.model.DataState;
import com.sharescrow.payment.model.Order;
import com.sun.tools.corba.se.idl.constExpr.Or;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface OrderRepository {

	Order selectOrderById(@Param("id") int id);

	Order selectOrderByTransactionId(@Param("transactionId") String transactionId);

	List<Order> selectOrderByUserId(@Param("userId") int userId);

	List<Order> selectOrderByGrouprId(@Param("groupId")int groupId);

	void update(Order order);

	void insert(Order order);

	void updateWhenGroupMatched(HashMap<String, Object> params);

	void updateWhenGroupExpired(HashMap<String, Object> params);
}
