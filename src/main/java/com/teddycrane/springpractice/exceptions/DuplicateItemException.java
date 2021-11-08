package com.teddycrane.springpractice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class DuplicateItemException extends ResponseStatusException {
	public DuplicateItemException(String message) {
		super(HttpStatus.BAD_REQUEST, message);
	}
}
