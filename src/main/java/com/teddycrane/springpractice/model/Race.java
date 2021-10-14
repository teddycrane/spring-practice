package com.teddycrane.springpractice.model;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

public class Race {

	private final HashMap<Integer, Racer> entries;
	private String name;

	public Race(String name, Racer @NotNull [] entries) {
		this.name = name;
		this.entries = new HashMap<>();

		for (int i = 0; i < entries.length; i++) {
			this.entries.put(i, entries[i]);
		}
	}

	public Race(String name) {
		this.name = name;
		this.entries = new HashMap<>();
	}

	public Race() {
		this("");
	}

	public Race(@NotNull Race race) {
		this(race.name, race.entries.values().toArray(new Racer[0]));
	}

	public Race(String name, HashMap<Integer, Racer> entries) {
		this.name = name;
		this.entries = new HashMap<>(entries);
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public HashMap<Integer, Racer> getEntries() {
		return new HashMap<Integer, Racer>(this.entries);
	}

	public Racer addEntry(Racer newEntry) {
		Integer[] keys = this.entries.keySet().toArray(new Integer[0]);
		int max = Arrays.stream(keys).max(Integer::compare).get();
		this.entries.put(max + 1, new Racer(newEntry));
		return newEntry;
	}

	public Racer addEntry(Racer newEntry, int number) throws IllegalAccessException {
		Set<Integer> keys = this.entries.keySet();
		if (keys.contains(number)) {
			throw new IllegalAccessException("An entry with number " + number + " already exists.  Please try again with a new number. ");
		} else {
			this.entries.put(number, new Racer(newEntry));
			return newEntry;
		}
	}

	public Racer getEntry(int number) throws ArrayIndexOutOfBoundsException {
		try {
			return new Racer(this.entries.get(number));
		} catch (Exception e) {
			throw new ArrayIndexOutOfBoundsException("No such racer with number " + number);
		}
	}

	public Racer removeEntry(int number) throws ArrayIndexOutOfBoundsException {
		try {
			return this.entries.remove(number);
		} catch (Exception e) {
			throw new ArrayIndexOutOfBoundsException("Failed to remove entry " + number + ". ");
		}
	}


}
