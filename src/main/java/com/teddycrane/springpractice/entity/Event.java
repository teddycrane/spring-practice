package com.teddycrane.springpractice.entity;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

@Entity
public class Event {

	@Id
	@Type(type = "uuid-char")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private final UUID id;

	@Column(unique = true)
	private String name;

	@OneToMany
	private Collection<Race> races;

	private Date startDate, endDate;

	public Event() {
		this.id = UUID.randomUUID();
		this.races = new ArrayList<>();
		this.startDate = new Date();
		this.endDate = new Date();
	}

	public Event(String name) {
		this();
		this.name = name;
	}

	public Event(String name, Date startDate, Date endDate) {
		this(name);
		this.startDate = new Date(startDate.getTime());
		this.endDate = new Date(endDate.getTime());
	}

	public Event(String name, Date startDate) {
		this(name, startDate, startDate);
	}

	public Event(Event other) {
		this.id = other.id;
		this.name = other.name;
		this.races = new ArrayList<>(other.races);
		this.startDate = new Date(other.startDate.getTime());
		this.endDate = new Date(other.endDate.getTime());
	}

	public UUID getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Collection<Race> getRaces() {
		return new ArrayList<>(this.races);
	}

	public void setRaces(Collection<Race> races) {
		this.races = new ArrayList<>(races);
	}

	public Date getStartDate() {
		return new Date(startDate.getTime());
	}

	public void setStartDate(Date startDate) {
		this.startDate = new Date(startDate.getTime());
	}

	public Date getEndDate() {
		return new Date(endDate.getTime());
	}

	public void setEndDate(Date endDate) {
		this.endDate = new Date(endDate.getTime());
	}

	public boolean equals(Event other) {
		return this.id.equals(other.id) && this.name.equals(other.name) && this.startDate.equals(other.startDate) && this.endDate.equals(other.endDate) && this.races.equals(other.races);
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{\n");
		builder.append(String.format("   \"id\": \"%s\",\n    \"name\": \"%s\",\n    \"startDate\": \"%s\",\n    \"endDate\": \"%s\",\n", id, name, startDate, endDate));
		builder.append("    \"races\": [\n");

		if (races.size() > 0) races.forEach((race) -> builder.append(String.format("%s,\n", race)));
		builder.append("    ],\n");
		builder.append("}");
		return builder.toString();
	}
}
