package com.sharescrow.sample.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ExceptionHandler;

import com.sharescrow.sample.common.exception.ApplicationError;
import com.sharescrow.sample.common.exception.ApplicationException;
import com.sharescrow.sample.model.response.BaseResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BaseRestController {
  @ExceptionHandler(ApplicationException.class)
  public BaseResponse applicationErrorHandler(HttpServletResponse response, ApplicationException exception) {
    ApplicationError errorCode = exception.getError();
    response.setStatus(errorCode.getHttpStatusCode());

    log.error(String.format("[%s]", errorCode.getCode()), exception);
    return new BaseResponse(errorCode.getCode());
  }
}
