package com.teddycrane.springpractice.user.request;


import javax.validation.constraints.NotNull;

public class AuthenticationRequest
{

	@NotNull
	private String password;

	@NotNull
	private String username;


	public String getPassword()
	{
		return password;
	}

	public String getUsername()
	{
		return username;
	}
}
