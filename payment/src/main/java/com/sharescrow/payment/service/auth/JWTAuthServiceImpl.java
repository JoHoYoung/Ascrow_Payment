package com.sharescrow.payment.service.auth;

import com.sharescrow.payment.ErrorCode;
import com.sharescrow.payment.exception.InvalidTokenException;
import com.sharescrow.payment.exception.TokenExpiredException;
import io.jsonwebtoken.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTAuthServiceImpl implements AuthService {
	@Value("escrow.jwt.secret")
	private String SALT;

	public String accessToken(String subject) {
		Date Now = new Date();
		Date expireTime = new Date(Now.getTime() + 1000 * 60 * 60 * 1);
		String jwt = Jwts.builder()
			.setExpiration(expireTime)
			.setSubject(subject)
			.signWith(SignatureAlgorithm.HS256, SALT)
			.compact();
		return jwt;
	}

	public String refreshToken(String subject) {
		Date Now = new Date();
		Date expireTime = new Date(Now.getTime() + 1000 * 60 * 60 * 24 * 2);
		String jwt = Jwts.builder()
			.setExpiration(expireTime)
			.setSubject(subject)
			.signWith(SignatureAlgorithm.HS256, SALT)
			.compact();
		return jwt;
	}

	public void verifyToken(String token) {
		try {
			Jwts.parser().setSigningKey(SALT).parseClaimsJws(token).getBody();
		} catch (ExpiredJwtException e) {
			throw new TokenExpiredException(ErrorCode.JWT_TOKEN_EXPIRED);
		} catch (JwtException e) {
			throw new InvalidTokenException(ErrorCode.INVALID_TOKEN);
		}
	}

	// Token 해독 및 객체 생성
	public String decode(String token) {
		Claims Claim = Jwts.parser().setSigningKey(SALT).parseClaimsJws(token).getBody();
		return Claim.getSubject();
	}

}
