package com.teddycrane.springpractice.models;

import com.sun.istack.Nullable;
import com.teddycrane.springpractice.enums.Category;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Optional;

public class CreateRaceRequest
{

	@NotNull
	private final String name;
	@NotNull
	private final Category category;

	@Nullable
	private final Date startTime, endTime;

	public CreateRaceRequest()
	{
		this.name = "";
		this.category = Category.CAT_5;
		this.startTime = null;
		this.endTime = null;
	}

	public CreateRaceRequest(String name, Category category)
	{
		this.name = name;
		this.category = category;
		this.startTime = null;
		this.endTime = null;
	}

	public CreateRaceRequest(String name, Category category, Date startTime)
	{
		this.name = name;
		this.category = category;
		this.startTime = new Date(startTime.getTime());
		this.endTime = null;
	}

	public CreateRaceRequest(String name, Category category, Date startTime, Date endTime)
	{
		this.name = name;
		this.category = category;
		this.startTime = new Date(startTime.getTime());
		this.endTime = new Date(endTime.getTime());
	}

	public Category getCategory()
	{
		return category;
	}

	public String getName()
	{
		return name;
	}

	public Optional<Date> getStartTime()
	{
		return Optional.ofNullable(startTime);
	}

	public Optional<Date> getEndTime()
	{
		return Optional.ofNullable(endTime);
	}
}
