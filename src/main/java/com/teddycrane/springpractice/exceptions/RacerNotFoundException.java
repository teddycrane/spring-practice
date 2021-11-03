package com.teddycrane.springpractice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class RacerNotFoundException extends ResponseStatusException {

	public RacerNotFoundException(String message) {
		super(HttpStatus.NOT_FOUND, message);
	}
}
