package com.teddycrane.springpractice.race.model;

import com.teddycrane.springpractice.racer.Racer;
import com.teddycrane.springpractice.enums.Category;

import java.util.ArrayList;
import java.util.List;

public class RaceResult
{

	private final List<Racer> results;
	private final String name;
	private final Category category;

	public RaceResult(String name, Category category, List<Racer> results)
	{
		this.name = name;
		this.category = category;
		this.results = new ArrayList<>(results);
	}

	public List<Racer> getResults()
	{
		return new ArrayList<>(results);
	}

	public String getName()
	{
		return name;
	}

	public Category getCategory()
	{
		return category;
	}
}
