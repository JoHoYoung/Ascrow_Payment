package com.sharescrow.payment.repository;

import com.sharescrow.payment.model.User;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository {

	List<User> selectUserByProductId(@Param("productId") int productId);

}
