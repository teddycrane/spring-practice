package com.teddycrane.springpractice.entity;

import com.teddycrane.springpractice.enums.Category;
import com.teddycrane.springpractice.helper.EnumHelpers;
import org.hibernate.annotations.Type;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
public class Race {

	@Id
	@Type(type = "uuid-char")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private final UUID id;

	@Column(unique = true)
	private String name;

	private Category category;

	@OneToMany
	private List<Racer> racers;

	public Race() {
		this.id = UUID.randomUUID();
		this.racers = new ArrayList<>();
	}

	public Race(@NotNull Race other) {
		this.id = other.id;
		this.name = other.name;
		this.category = other.category;
		this.racers = new ArrayList<>(other.racers);
	}

	public Race(String name, Category category) {
		this();
		this.name = name;
		this.category = category;
	}

	private Race(String name, Category category, Collection<Racer> racers) {
		this(name, category);
		this.racers = new ArrayList<>(racers);
	}

	public Collection<Racer> getRacers() {
		return racers;
	}

	public void setRacers(Collection<Racer> racers) {
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

	public Racer addRacer(Racer r) {
		this.racers.add(new Racer(r));
		return r;
	}

	public boolean equals(@NotNull Race other) {
		boolean primitiveCompare = this.id == other.id && this.name.equals(other.name) && this.category == other.category;

		// skip expensive list comparison if the other race does not have the same ID as this one.
		if (primitiveCompare && this.racers.size() == other.racers.size()) {
			// todo update this to actually compare the order as well
			return this.racers.containsAll(other.racers);
		}

		return primitiveCompare;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{\n");
		builder.append(String.format("    \"name\": \"%s\",\n", this.name));
		builder.append(String.format("    \"id\": \"%s\",\n", this.id.toString()));
		builder.append(String.format("    \"category\": \"%s\",\n", EnumHelpers.getCategoryMapping(this.category)));
		builder.append("    \"riders\": [\n");
		if (racers.size() > 0) {
			for (int i = 0; i < this.racers.size(); i++) {
				builder.append(String.format("        %s", this.racers.get(i).toString()));
				if (i != this.racers.size()) builder.append(",");
				builder.append("\n");
			}
		}
		builder.append("    ]\n");
		builder.append("}");
		return builder.toString();
	}
}
