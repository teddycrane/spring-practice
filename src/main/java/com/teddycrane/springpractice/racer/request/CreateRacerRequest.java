package com.teddycrane.springpractice.racer.request;

public class CreateRacerRequest
{

	private final String firstName;
	private final String lastName;

	public CreateRacerRequest()
	{
		firstName = "";
		lastName = "";
	}

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
