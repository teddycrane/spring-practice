package com.teddycrane.springpractice.error;

public class BadRequestException extends BaseRequestException {
  public BadRequestException(String message) { super(message); }
}
