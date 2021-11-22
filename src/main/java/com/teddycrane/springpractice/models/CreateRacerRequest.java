package com.teddycrane.springpractice.models;

public class CreateRacerRequest
{

	private final String firstName;
	private final String lastName;

	public CreateRacerRequest(String firstName, String lastName)
	{
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public String getLastName()
	{
		return lastName;
	}
}
