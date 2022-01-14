package com.teddycrane.springpractice.error;

public class IllegalSearchTypeException extends BaseRequestException {
  public IllegalSearchTypeException(String message) {
    super(message);
  }
}
