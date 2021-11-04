package com.teddycrane.springpractice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CreationException extends BaseRequestException {
	public CreationException(String message) {
		super(message);
	}
}
