package com.teddycrane.springpractice.exceptions;

public class UserNotFoundError extends BaseNotFoundException
{

	public UserNotFoundError(String message)
	{
		super(message);
	}
}
