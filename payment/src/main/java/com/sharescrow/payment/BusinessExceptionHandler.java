package com.sharescrow.payment;

import javax.servlet.http.HttpServletRequest;

import com.sharescrow.payment.exception.BusinessException;
import com.sharescrow.payment.exception.BusinessException;
import com.sharescrow.payment.exception.BusinessException;
import com.sharescrow.payment.exception.BusinessException;
import com.sharescrow.payment.exception.BusinessException;
import com.sharescrow.payment.exception.BusinessException;
import com.sharescrow.payment.exception.BusinessException;
import com.sharescrow.payment.exception.BusinessException;
import com.sharescrow.payment.exception.BusinessException;
import com.sharescrow.payment.exception.BusinessException;
import com.sharescrow.payment.exception.BusinessException;
import com.sharescrow.payment.exception.BusinessException;
import com.sharescrow.payment.exception.BusinessException;
import com.sharescrow.payment.exception.BusinessException;
import com.sharescrow.payment.exception.BusinessException;
import com.sharescrow.payment.exception.BusinessException;
import com.sharescrow.payment.exception.BusinessException;
import com.sharescrow.payment.exception.BusinessException;
import com.sharescrow.payment.exception.BusinessException;
import com.sharescrow.payment.exception.BusinessException;
import com.sharescrow.payment.response.ErrorResponse;
import com.sharescrow.payment.util.Util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BusinessExceptionHandler {

	private final Log logger = LogFactory.getLog(this.getClass());

	@ExceptionHandler(BusinessException.class)
	protected ResponseEntity<ErrorResponse> BusinessExceptionHandler(HttpServletRequest request, BusinessException e) {
		StringBuilder errLog = new StringBuilder();
		errLog
			.append("[REQUEST] ")
			.append("Uri : ")
			.append(request.getRequestURI())
			.append(", Method : ")
			.append(request.getMethod())
			.append("query parmas : ")
			.append(request.getQueryString())
			.append(", Request Body : ")
			.append(Util.extractPostRequestBody(request));
		logger.error(errLog.toString());
		return new ResponseEntity<>(new ErrorResponse(e.getErrorCode()), e.getStatusCode());
	}
}

