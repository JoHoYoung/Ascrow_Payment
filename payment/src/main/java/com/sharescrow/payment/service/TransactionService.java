package com.sharescrow.payment.service;

import com.sharescrow.payment.exception.ErrorCode;
import com.sharescrow.payment.exception.BusinessException;
import com.sharescrow.payment.model.Transaction;
import com.sharescrow.payment.repository.TransactionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class TransactionService {

	@Autowired
	TransactionRepository transactionRepository;

	public void creatTransaction(Transaction transaction) {
		transactionRepository.insert(transaction);
	}

	public Transaction getTransactionById(String id) {
		Transaction transaction = transactionRepository.selectById(id);
		if (Objects.isNull(transaction)) {
			throw new BusinessException(ErrorCode.EMPTY_DATA_SET);
		}
		return transaction;
	}

	public void update(String transactionId, Transaction transaction) {
		transactionRepository.update(transactionId, transaction);
	}

}

