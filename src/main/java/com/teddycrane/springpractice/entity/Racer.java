package com.teddycrane.springpractice.entity;

import com.teddycrane.springpractice.enums.Category;
import com.teddycrane.springpractice.helper.EnumHelpers;
import org.hibernate.annotations.Type;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class Racer
{

	@Id
	@Type(type = "uuid-char")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private final UUID id;
	private String firstName = "";
	private String lastName = "";
	@Enumerated(EnumType.STRING)
	private Category category = Category.CAT5;
	private boolean isDeleted = false;

	private Racer(UUID id, String firstName, String lastName, Category category, boolean isDeleted)
	{
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.category = category;
		this.isDeleted = isDeleted;
	}

	public Racer()
	{
		this.id = UUID.randomUUID();
	}

	public Racer(@NotNull Racer other)
	{
		this(other.id, other.firstName, other.lastName, other.category, other.isDeleted);
	}

	public Racer(String firstName, String lastName)
	{
		this();
		this.firstName = firstName;
		this.lastName = lastName;
		this.category = Category.CAT5;
	}

	public UUID getId()
	{
		return id;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public Category getCategory()
	{
		return category;
	}

	public void setCategory(Category category)
	{
		this.category = category;
	}

	public boolean getIsDeleted()
	{
		return this.isDeleted;
	}

	public void setIsDeleted(boolean isDeleted)
	{
		this.isDeleted = isDeleted;
	}

	public boolean equals(@NotNull Racer other)
	{
		return this.id == other.id &&
				this.category == other.category &&
				this.firstName.equals(other.firstName) && this.lastName.equals(other.lastName) &&
				this.isDeleted == other.isDeleted;
	}

	@Override
	public String toString()
	{
		return "{\n" +
				String.format("    \"id\": \"%s\",\n", this.id.toString()) +
				String.format("    \"firstName\": \"%s\",\n", this.firstName) +
				String.format("    \"lastName\": \"%s\",\n", this.lastName) +
				String.format("    \"category\": \"%s\",\n", EnumHelpers.getCategoryMapping(this.category)) +
				String.format("    \"isDeleted\": \"%s\"\n", this.isDeleted) +
				"}";
	}

	@Override
	public int hashCode()
	{
		int hash = 7;
		hash = 31 * hash + this.id.hashCode();
		hash = 31 * hash + this.firstName.hashCode();
		hash = 31 * hash + this.lastName.hashCode();
		hash = 31 * hash + this.category.hashCode();
		if (this.isDeleted) hash++;

		return hash;
	}
}
