package com.teddycrane.springpractice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class StartException extends ResponseStatusException
{
	public StartException(String message)
	{
		super(HttpStatus.CONFLICT, message);
	}
}
