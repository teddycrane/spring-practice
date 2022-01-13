package com.teddycrane.springpractice.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NoCredentialsException extends ResponseStatusException
{
	public NoCredentialsException(String message)
	{
		super(HttpStatus.UNAUTHORIZED, message);
	}
}
