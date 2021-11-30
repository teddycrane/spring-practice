package com.teddycrane.springpractice.enums;

public enum FilterType
{
	NAME_CONTAINS("name-contains"),
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
