package com.teddycrane.springpractice.model;

import com.teddycrane.springpractice.enums.Category;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class Racer {

	private final String id;
	private String firstName, lastName;
	private Category category;

	public Racer(String firstName, String lastName, Category category) {
		this.id = UUID.randomUUID().toString();
		this.firstName = firstName;
		this.lastName = lastName;
		this.category = category;
	}

	public Racer(@NotNull Racer racer) {
		this(racer.firstName, racer.lastName, racer.category, racer.id);
	}

	private Racer(String firstName, String lastName, Category category, String id) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.category = category;
	}

	public String getId() {
		return this.id;
	}

	public String getName() {
		return firstName + " " + lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Category getCategory() {
		return this.category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String toString() {
		return this.firstName + " " + this.lastName;
	}
}
