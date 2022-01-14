package com.teddycrane.springpractice.error;

public class UpdateException extends BaseRequestException {
  public UpdateException(String message) {
    super(message);
  }
}
