package com.teddycrane.springpractice.error;

/**
 * Extends 404 - Not Found
 */
public class RacerNotFoundException extends BaseNotFoundException {

	public RacerNotFoundException(String message) {
		super(message);
	}
}
