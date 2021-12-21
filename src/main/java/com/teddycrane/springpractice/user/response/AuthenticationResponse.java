package com.teddycrane.springpractice.user.response;

public class AuthenticationResponse
{

	private boolean authenticated;

	private String token;
	
	// todo add expiry as a instance variable

	public AuthenticationResponse()
	{
		this.authenticated = false;
		this.token = "";
	}

	public AuthenticationResponse(boolean authenticated, String token)
	{
		this.authenticated = authenticated;
		this.token = token;
	}

	public AuthenticationResponse(boolean authenticated)
	{
		this.authenticated = authenticated;
		this.token = "";
	}

	public boolean isAuthenticated()
	{
		return authenticated;
	}

	public void setAuthenticated(boolean authenticated)
	{
		this.authenticated = authenticated;
	}

	public void setToken(String token)
	{
		this.token = token;
	}

	public String getToken()
	{
		return this.token;
	}
}
