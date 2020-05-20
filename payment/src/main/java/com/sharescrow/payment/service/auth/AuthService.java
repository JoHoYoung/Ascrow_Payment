package com.sharescrow.payment.service.auth;

import com.sharescrow.payment.ErrorCode;
import com.sharescrow.payment.exception.InvalidTokenException;
import com.sharescrow.payment.exception.TokenExpiredException;
import io.jsonwebtoken.*;

import java.util.Date;

public interface AuthService {
	String accessToken(String subject);

	String refreshToken(String subject);

	void verifyToken(String token);

	String decode(String token);
}
