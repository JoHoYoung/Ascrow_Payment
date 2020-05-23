package com.sharescrow.payment.service.pay;

import com.sharescrow.payment.model.Order;
import com.sharescrow.payment.model.Transaction;

public interface PayService<T1,T2> {
	T1 execute(Order order);

	T2 cancel(Order order, Transaction transaction);
}
