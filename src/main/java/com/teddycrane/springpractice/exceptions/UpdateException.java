package com.teddycrane.springpractice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UpdateException extends BaseRequestException {
	public UpdateException(String message) {
		super(message);
	}
}
