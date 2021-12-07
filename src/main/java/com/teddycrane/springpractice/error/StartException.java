package com.teddycrane.springpractice.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class StartException extends ResponseStatusException
{
	public StartException(String message)
	{
		super(HttpStatus.CONFLICT, message);
	}
}
