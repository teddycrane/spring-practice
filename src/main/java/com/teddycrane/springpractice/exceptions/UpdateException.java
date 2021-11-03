package com.teddycrane.springpractice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UpdateException extends ResponseStatusException {
	public UpdateException(String message) {
		super(HttpStatus.BAD_REQUEST, message);
	}
}
