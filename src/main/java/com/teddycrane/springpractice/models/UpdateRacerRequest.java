package com.teddycrane.springpractice.models;

import com.teddycrane.springpractice.enums.Category;
import com.teddycrane.springpractice.helper.EnumHelpers;
import org.springframework.lang.Nullable;

import java.util.Optional;

public class UpdateRacerRequest {

	@Nullable
	private String firstName, lastName;
	@Nullable
	private Category category;

	public Optional<String> getFirstName() {
		return Optional.ofNullable(firstName);
	}

	public Optional<String> getLastName() {
		return Optional.ofNullable(lastName);
	}

	public Optional<Category> getCategory() {
		return Optional.ofNullable(category);
	}

	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("{\n");
		this.getFirstName().ifPresent(s -> result.append(String.format("    \"firstName\": \"%s\",\n", s)));
		this.getLastName().ifPresent(s -> result.append(String.format("    \"lastName\": \"%s\",\n", s)));
		this.getCategory().ifPresent(s -> result.append(String.format("    \"category\": \"%s\",\n", EnumHelpers.getCategoryMapping(s))));
		result.append("}");
		return result.toString();
	}
}
