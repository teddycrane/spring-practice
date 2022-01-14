package com.teddycrane.springpractice.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class InternalServerError extends HttpStatusCodeException {

  public InternalServerError() {
    super(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
  }

  public InternalServerError(String message) {
    super(HttpStatus.INTERNAL_SERVER_ERROR, message);
  }
}
