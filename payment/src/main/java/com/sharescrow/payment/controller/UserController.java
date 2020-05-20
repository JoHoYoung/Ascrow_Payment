package com.sharescrow.payment.controller;

import com.sharescrow.payment.model.User;
import com.sharescrow.payment.response.BaseResponse;
import com.sharescrow.payment.response.DataListResponse;
import com.sharescrow.payment.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



import java.util.List;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

	@Autowired
	UserService userService;

	@GetMapping("/list/product")
	public ResponseEntity<BaseResponse> getListByProductId(@Valid @RequestParam("productId") int productId) {
		List<User> userList = userService.getUsersByProductId(productId);
		final BaseResponse response = new DataListResponse<>(200, "success", userList);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
