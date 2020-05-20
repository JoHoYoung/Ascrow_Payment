package com.sharescrow.payment.service;

import com.sharescrow.payment.ErrorCode;
import com.sharescrow.payment.exception.EmptyDataException;
import com.sharescrow.payment.model.Order;
import com.sharescrow.payment.repository.OrderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class OrderService {

	@Autowired
	OrderRepository orderRepository;

	public Order getOrderById(int orderId) {
		Order order = orderRepository.selectOrderById(orderId);
		if (Objects.isNull(order)) {
			throw new EmptyDataException(ErrorCode.EMPTY_DATA_SET);
		}
		return order;
	}

	public List<Order> getOrdersByUserId(int userId) {
		List<Order> orderList = orderRepository.selectOrderByUserId(userId);
		if (orderList.isEmpty()) {
			throw new EmptyDataException(ErrorCode.EMPTY_DATA_SET);
		}
		return orderList;
	}

	public List<Order> getOrdersByGroupId(int groupId) {
		List<Order> orderList = orderRepository.selectOrderByGrouprId(groupId);
		if (orderList.isEmpty()) {
			throw new EmptyDataException(ErrorCode.EMPTY_DATA_SET);
		}
		return orderList;
	}

	public Order getOrderByTransactionId(String transactionId) {
		Order order = orderRepository.selectOrderByTransactionId(transactionId);
		if (Objects.isNull(order)) {
			throw new EmptyDataException(ErrorCode.EMPTY_DATA_SET);
		}
		return order;
	}

	public void createOrder(Order order) {
		orderRepository.insert(order);
	}

	public void updateOrder(Order order){orderRepository.update(order);}
}
