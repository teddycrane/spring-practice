package com.teddycrane.springpractice.exceptions;

public class BadRequestException extends BaseRequestException{
	public BadRequestException(String message) {
		super(message);
	}
}
