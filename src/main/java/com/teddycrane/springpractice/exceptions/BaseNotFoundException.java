package com.teddycrane.springpractice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class BaseNotFoundException extends ResponseStatusException {
	public BaseNotFoundException(String message) {
		super(HttpStatus.NOT_FOUND, message);
	}
}
