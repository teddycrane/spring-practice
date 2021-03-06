package com.teddycrane.springpractice.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EndException extends ResponseStatusException {
  public EndException(String message) {
    super(HttpStatus.BAD_REQUEST, message);
  }
}
