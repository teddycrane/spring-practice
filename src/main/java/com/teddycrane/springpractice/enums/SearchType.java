package com.teddycrane.springpractice.enums;

public enum SearchType
{
	NAME("Name"),
	CATEGORY("Category");

	private String text;

	SearchType(final String text)
	{
		this.text = text;
	}

	public String toString()
	{
		return this.text;
	}
}
