package com.teddycrane.springpractice.enums;

public enum FilterType
{
	FIRSTNAME("firstname"),
	LASTNAME("lastname"),
	CATEGORY("category");

	private final String text;

	FilterType(final String text)
	{
		this.text = text;
	}

	public String toString()
	{
		return this.text;
	}
}
