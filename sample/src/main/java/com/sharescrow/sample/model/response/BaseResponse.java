package com.sharescrow.sample.model.response;

import java.util.Collections;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BaseResponse <T> {
  public int resultCode = 200;
  public List<T> result;

  public BaseResponse(int resultCode) {
    this.resultCode = resultCode;
    this.result = Collections.emptyList();
  }

  public BaseResponse(List<T> result) {
    this.result = result;
  }

  public BaseResponse(T result) {
    this.result = Collections.singletonList(result);
  }
}
