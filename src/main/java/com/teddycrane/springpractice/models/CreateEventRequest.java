package com.teddycrane.springpractice.models;

import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Optional;

public class CreateEventRequest
{

	@NotNull
	private String name = "";

	@Nullable
	private final Date startDate;
	@Nullable
	private final Date endDate;

	public CreateEventRequest()
	{
		name = "";
		startDate = null;
		endDate = null;
	}

	public CreateEventRequest(String name, Date startDate, Date endDate)
	{
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
	}


	public String getName()
	{
		return name;
	}

	public Optional<Date> getStartDate()
	{
		return Optional.ofNullable(this.startDate);
	}

	public Optional<Date> getEndDate()
	{
		return Optional.ofNullable(this.endDate);
	}
}
