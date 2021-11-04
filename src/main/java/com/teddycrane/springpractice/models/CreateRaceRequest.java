package com.teddycrane.springpractice.models;

import com.teddycrane.springpractice.enums.Category;

public class CreateRaceRequest {
	private String name;
	private Category category;

	public Category getCategory() {
		return category;
	}

	public String getName() {
		return name;
	}
}
