package com.teddycrane.springpractice.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NotAuthenticatedException extends ResponseStatusException
{
	public NotAuthenticatedException(String message)
	{
		super(HttpStatus.UNAUTHORIZED, message);
	}
}
