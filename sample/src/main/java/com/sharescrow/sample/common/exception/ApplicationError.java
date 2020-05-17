package com.sharescrow.sample.common.exception;

import lombok.Getter;

public enum ApplicationError {
  NOT_FOUND(404, 404),
  BAD_REQUEST(400, 400),
  FAIL_TO_START_JOB(5001, 500);;

  @Getter
  private int code;
  @Getter
  private int httpStatusCode = 500;
  ApplicationError(int code, int httpStatusCode) {
    this.code = code;
    this.httpStatusCode = httpStatusCode;
  }
  ApplicationError(int code) {
    this.code = code;
  }

}
