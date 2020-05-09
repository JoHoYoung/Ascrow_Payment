package com.sharescrow.sample.common.exception;

import lombok.Getter;

public class ApplicationException extends RuntimeException {
  @Getter
  private ApplicationError error;

  public ApplicationException(ApplicationError error) {
    this.error = error;
  }

  public ApplicationException(ApplicationError error, String errorMessage) {
    super(errorMessage);
    this.error = error;
  }

  public ApplicationException(ApplicationError error, String errorMessageFormat, Object ... messageArguments) {
    super(String.format(errorMessageFormat, messageArguments));
    this.error = error;
  }
}
