package com.teddycrane.springpractice.models;

import com.teddycrane.springpractice.enums.Category;
import org.springframework.lang.Nullable;

import java.util.Optional;

public class UpdateRaceRequest
{

	@Nullable
	private final String name;

	@Nullable
	private final Category category;

	public UpdateRaceRequest(@Nullable String name, @Nullable Category category)
	{
		this.name = name;
		this.category = category;
	}

	public Optional<String> getName()
	{
		return Optional.ofNullable(name);
	}

	public Optional<Category> getCategory()
	{
		return Optional.ofNullable(category);
	}
}
