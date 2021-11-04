package com.teddycrane.springpractice.models;

import com.teddycrane.springpractice.enums.Category;
import org.springframework.lang.Nullable;

import java.util.Optional;

public class UpdateRaceRequest {

	@Nullable
	private String name;

	@Nullable
	private Category category;

	public Optional<String> getName() {
		return Optional.ofNullable(name);
	}

	public Optional<Category> getCategory() {
		return Optional.ofNullable(category);
	}
}
