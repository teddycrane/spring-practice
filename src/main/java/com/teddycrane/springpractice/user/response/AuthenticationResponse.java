package com.teddycrane.springpractice.user.response;

public class AuthenticationResponse
{

	private boolean authenticated;

	public AuthenticationResponse()
	{
		this.authenticated = true;
	}

	public AuthenticationResponse(boolean authenticated)
	{
		this.authenticated = authenticated;
	}

	public boolean isAuthenticated()
	{
		return authenticated;
	}

	public void setAuthenticated(boolean authenticated)
	{
		this.authenticated = authenticated;
	}
}
