package com.teddycrane.springpractice.service;

import com.sun.istack.Nullable;
import com.teddycrane.springpractice.entity.Racer;
import com.teddycrane.springpractice.enums.Category;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface IRacerService {

	/**
	 * Gets all racers from DB
	 * @return a List of all the racers in the DB
	 */
	List<Racer> getAllRacers();

	/**
	 * Gets a racer by ID
	 * @param id The UUID of the racer to be found
	 * @return An Optional object wrapping a Racer that isPresent if the racer is found.
	 */
	Optional<Racer> getRacerById(UUID id);

	Racer addRacer(String firstName, String lastName);

	Racer updateRacer(UUID id, String firstName, String lastName, Category category);

	Racer deleteRacer(UUID id);
}
