package com.teddycrane.springpractice.health;

public class Health
{

	private final String status;

	private final String name;

	public Health()
	{
		this.status = "healthy";
		this.name = "";
	}

	public Health(String name)
	{
		this.status = "healthy";
		this.name = name;
	}

	public String getStatus()
	{
		return status;
	}

	public String getName()
	{
		return name;
	}
}
