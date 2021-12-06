package com.teddycrane.springpractice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class InternalServerError extends HttpStatusCodeException
{

	public InternalServerError(String message)
	{
		super(HttpStatus.INTERNAL_SERVER_ERROR, message);
	}
}
