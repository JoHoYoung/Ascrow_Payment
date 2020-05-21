package com.sharescrow.payment;

import com.sharescrow.payment.exception.DateParseException;
import com.sharescrow.payment.exception.DecodedTokenParseException;
import com.sharescrow.payment.exception.EmptyDataException;
import com.sharescrow.payment.exception.InternalServerException;
import com.sharescrow.payment.exception.InvalidAuthException;
import com.sharescrow.payment.exception.InvalidOrderRequestException;
import com.sharescrow.payment.exception.InvalidParameterException;
import com.sharescrow.payment.exception.InvalidPayTypeException;
import com.sharescrow.payment.exception.InvalidTokenException;
import com.sharescrow.payment.exception.NotFoundException;
import com.sharescrow.payment.exception.OrderFailException;
import com.sharescrow.payment.exception.PayReadyException;
import com.sharescrow.payment.exception.PayTransactionException;
import com.sharescrow.payment.exception.ProductInvalidException;
import com.sharescrow.payment.exception.TokenExpiredException;
import com.sharescrow.payment.exception.TransactionCancelException;
import com.sharescrow.payment.exception.TransactionCancelFailException;
import com.sharescrow.payment.exception.TransactionFailException;
import com.sharescrow.payment.exception.UnSupportedOperationException;
import com.sharescrow.payment.exception.UnauthorizedAccessException;
import com.sharescrow.payment.response.ErrorResponse;

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

	@ExceptionHandler(EmptyDataException.class)
	protected ResponseEntity<ErrorResponse> EmptyDataExceptionHandler(EmptyDataException e) {
		return new ResponseEntity<>(new ErrorResponse(e.getErrorCode()), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	protected ResponseEntity<ErrorResponse> MissingServletRequestParameterExceptionHandler(
		MissingServletRequestParameterException e) {
		return new ResponseEntity<>(new ErrorResponse(ErrorCode.INVALID_REQUEST_PARAM), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(DateParseException.class)
	protected ResponseEntity<ErrorResponse> DateParseExceptionHandler(DateParseException e){
		return new ResponseEntity<>(new ErrorResponse(e.getErrorCode()), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(DecodedTokenParseException.class)
	protected ResponseEntity<ErrorResponse> DecodedTokenParseExceptionHandler(DecodedTokenParseException e){
		return new ResponseEntity<>(new ErrorResponse(e.getErrorCode()), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(InternalServerException.class)
	protected ResponseEntity<ErrorResponse> InternalServerExceptionHandler(InternalServerException e){
		return new ResponseEntity<>(new ErrorResponse(e.getErrorCode()), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(InvalidAuthException.class)
	protected ResponseEntity<ErrorResponse> InvalidAuthExceptionHandler(InvalidAuthException e){
		return new ResponseEntity<>(new ErrorResponse(e.getErrorCode()), HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(InvalidOrderRequestException.class)
	protected ResponseEntity<ErrorResponse> InvalidOrderRequestExceptionHandler(InvalidOrderRequestException e){
		return new ResponseEntity<>(new ErrorResponse(e.getErrorCode()), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(InvalidPayTypeException.class)
	protected ResponseEntity<ErrorResponse> InvalidPayTypeExceptionHandler(InvalidPayTypeException e){
		return new ResponseEntity<>(new ErrorResponse(e.getErrorCode()), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(InvalidTokenException.class)
	protected ResponseEntity<ErrorResponse> InvalidTokenExceptionHandler(InvalidTokenException e){
		return new ResponseEntity<>(new ErrorResponse(e.getErrorCode()), HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(NotFoundException.class)
	protected ResponseEntity<ErrorResponse> NotFoundExceptionHandler(NotFoundException e){
		return new ResponseEntity<>(new ErrorResponse(e.getErrorCode()), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(OrderFailException.class)
	protected ResponseEntity<ErrorResponse> OrderFailExceptionHandler(OrderFailException e){
		return new ResponseEntity<>(new ErrorResponse(e.getErrorCode()), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(PayReadyException.class)
	protected ResponseEntity<ErrorResponse> PayReadyExceptionHandler(PayReadyException e){
		return new ResponseEntity<>(new ErrorResponse(e.getErrorCode()), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(PayTransactionException.class)
	protected ResponseEntity<ErrorResponse> PayTransactionExceptionHandler(PayTransactionException e){
		return new ResponseEntity<>(new ErrorResponse(e.getErrorCode()), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(TokenExpiredException.class)
	protected ResponseEntity<ErrorResponse> TokenExpiredExceptionHandler(TokenExpiredException e){
		return new ResponseEntity<>(new ErrorResponse(e.getErrorCode()), HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(TransactionCancelException.class)
	protected ResponseEntity<ErrorResponse> TransactionCancelExceptionHandler(TransactionCancelException e){
		return new ResponseEntity<>(new ErrorResponse(e.getErrorCode()), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(TransactionFailException.class)
	protected ResponseEntity<ErrorResponse> TransactionFailExceptionHandler(TransactionFailException e){
		return new ResponseEntity<>(new ErrorResponse(e.getErrorCode()), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(UnauthorizedAccessException.class)
	protected ResponseEntity<ErrorResponse> UnauthorizedAccessExceptionHandler(UnauthorizedAccessException e){
		return new ResponseEntity<>(new ErrorResponse(e.getErrorCode()), HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(UnSupportedOperationException.class)
	protected ResponseEntity<ErrorResponse> UnSupportedOperationExceptionHandler(UnSupportedOperationException e){
		return new ResponseEntity<>(new ErrorResponse(e.getErrorCode()), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ProductInvalidException.class)
	protected ResponseEntity<ErrorResponse> ProductInvalidExceptionHandler(ProductInvalidException e){
		return new ResponseEntity<>(new ErrorResponse(e.getErrorCode()), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(InvalidParameterException.class)
	protected ResponseEntity<ErrorResponse> InvalidParameterExceptionHandler(InvalidParameterException e){
		return new ResponseEntity<>(new ErrorResponse(e.getErrorCode()), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(TransactionCancelFailException.class)
	protected ResponseEntity<ErrorResponse> TransactionCancelFailExceptionHandler(TransactionCancelFailException e){
		return new ResponseEntity<>(new ErrorResponse(e.getErrorCode()), HttpStatus.INTERNAL_SERVER_ERROR);
	}

}

