package com.sharescrow.payment.service.auth;


public interface AuthService {
	String accessToken(String subject);

	String refreshToken(String subject);

	void verifyToken(String token);

	String decode(String token);
}
