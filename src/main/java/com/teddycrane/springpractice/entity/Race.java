package com.teddycrane.springpractice.entity;

import com.teddycrane.springpractice.enums.Category;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.*;

@Entity
public class Race {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private final UUID id;

	private String name;

	private Category category;

	private Collection<UUID> racers;

	public Race() {
		this.id = UUID.randomUUID();
		this.racers = new ArrayList<>();
	}

	private Race(String name, Category category, Collection<Racer> racers) {
		this();
		this.name = name;
		this.category = category;
		this.racers = new ArrayList<>();
		if (racers != null) {
			racers.forEach(racer -> this.racers.add(racer.getId()));
		}
	}

	public Collection<UUID> getRacers() {
		return racers;
	}

	public void setRacers(Collection<UUID> racers) {
		this.racers = new ArrayList<>(racers);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public UUID getId() {
		return this.id;
	}
}
