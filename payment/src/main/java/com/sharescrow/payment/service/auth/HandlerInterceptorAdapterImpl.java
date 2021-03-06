package com.sharescrow.payment.service.auth;

import com.sharescrow.payment.exception.ErrorCode;
import com.sharescrow.payment.context.Session;
import com.sharescrow.payment.exception.BusinessException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

@Component
public class HandlerInterceptorAdapterImpl extends HandlerInterceptorAdapter {
	@Autowired
	AuthService authService;

	@Autowired
	ObjectMapper objectMapper;

	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) {
		try {
			if (req.getHeader("Authorization") == null) {
				throw new BusinessException(ErrorCode.EMPTY_TOKEN);
			}
			String[] authHeader = req.getHeader("Authorization").split(" ");
			String token = authHeader[1];
			authService.verifyToken(token);
			Session session = objectMapper.readValue(authService.decode(token), Session.class);
			req.setAttribute("session", session);
			return true;
		} catch (IOException e) {
			throw new BusinessException(ErrorCode.DECODED_TOKEN_PARSE_ERROR);
		} catch (IndexOutOfBoundsException e){
			throw new BusinessException(ErrorCode.UNAUTHORIZED_ACCESS);
		}
	}
}
