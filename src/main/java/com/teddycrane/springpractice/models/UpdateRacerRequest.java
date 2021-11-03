package com.teddycrane.springpractice.models;

import com.teddycrane.springpractice.enums.Category;

public class UpdateRacerRequest extends CreateRacerRequest {
	private Category category;

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
}
