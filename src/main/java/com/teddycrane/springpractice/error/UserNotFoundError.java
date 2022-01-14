package com.teddycrane.springpractice.error;

public class UserNotFoundError extends BaseNotFoundException {

  public UserNotFoundError(String message) {
    super(message);
  }
}
