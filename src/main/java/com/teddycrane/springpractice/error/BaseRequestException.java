package com.teddycrane.springpractice.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class BaseRequestException extends ResponseStatusException {
  protected BaseRequestException(String message) {
    super(HttpStatus.BAD_REQUEST, message);
  }
}
