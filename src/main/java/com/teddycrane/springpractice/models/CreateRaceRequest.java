package com.teddycrane.springpractice.models;

import com.teddycrane.springpractice.enums.Category;

import javax.validation.constraints.NotNull;

public class CreateRaceRequest
{

	@NotNull
	private final String name;
	@NotNull
	private final Category category;

	public CreateRaceRequest(String name, Category category)
	{
		this.name = name;
		this.category = category;
	}

	public Category getCategory()
	{
		return category;
	}

	public String getName()
	{
		return name;
	}
}
