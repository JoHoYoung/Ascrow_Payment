package com.sharescrow.payment.service;

import com.sharescrow.payment.ErrorCode;
import com.sharescrow.payment.exception.EmptyDataException;
import com.sharescrow.payment.model.User;
import com.sharescrow.payment.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;

	public List<User> getUsersByProductId(int productId) {
		List<User> userList = userRepository.selectUserByProductId(productId);
		if (userList.isEmpty()) {
			throw new EmptyDataException(ErrorCode.EMPTY_DATA_SET);
		}
		return userList;
	}

}
