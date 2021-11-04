package com.teddycrane.springpractice.controller;

import com.teddycrane.springpractice.entity.Racer;
import com.teddycrane.springpractice.enums.Category;
import com.teddycrane.springpractice.exceptions.CreationException;
import com.teddycrane.springpractice.exceptions.RacerNotFoundException;
import com.teddycrane.springpractice.exceptions.UpdateException;
import com.teddycrane.springpractice.models.CreateRacerRequest;
import com.teddycrane.springpractice.models.UpdateRacerRequest;
import com.teddycrane.springpractice.repository.RacerRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping(path = "/racer")
public class RacerController {

	@Autowired
	private RacerRepository racerRepository;

	@GetMapping(path = "/all")
	public @ResponseBody
	Iterable<Racer> getAllRacers() {
		return racerRepository.findAll();
	}

	@GetMapping
	public @ResponseBody
	Racer getRacer(@RequestParam String id) throws RacerNotFoundException {
		System.out.printf("getRacer called with id %s", id);
		try {
			Racer r;
			UUID uuid = UUID.fromString(id);
			Optional<Racer> queryResult = racerRepository.findById(uuid);
			// allowing the get() call without an isPresent() check since we want an error to be thrown from the catch if no element is found
			return new Racer(queryResult.get());
		} catch (Exception e) {
			throw new RacerNotFoundException(String.format("No racer found with the id %s.", id));
		}
	}

	@PostMapping(path = "/new")
	public @ResponseBody
	Racer addRacer(@RequestBody @NotNull CreateRacerRequest request) throws CreationException {
		System.out.println("addRacer called");
		Racer r = new Racer();
		r.setCategory(Category.CAT_5);
		r.setFirstName(request.getFirstName());
		r.setLastName(request.getLastName());

		// duplication check
		try {
			List<Racer> existing = racerRepository.findByFirstNameAndLastName(r.getFirstName(), r.getLastName());
			Racer existingRacer = existing.get(0);
			if (existingRacer.getFirstName().equals(r.getFirstName()) && existingRacer.getLastName().equals(r.getLastName())) {
				throw new CreationException(String.format("Cannot create a new racer with name %s %s", request.getFirstName(), request.getLastName()));
			}
		} catch (IndexOutOfBoundsException ignored) {
		}
		racerRepository.save(r);
		return r;
	}

	/**
	 * Handles PATCH requests to /racer/update?id=racerId
	 * @param request The request object containing the fields to update
	 * @param id The id of the racer to update
	 * @return The updated Racer object
	 * @throws UpdateException Thrown if the racer does not exist, or if the racer fails to update
	 */
	@PatchMapping(path = "/update")
	public @ResponseBody
	Racer updateRacer(@RequestBody @NotNull UpdateRacerRequest request, @RequestParam String id) throws UpdateException {
		try {
			System.out.printf("updateRacer called with id %s \nand request body \n%s\n", id, request);
			UUID racerId = UUID.fromString(id);
			Optional<Racer> _racer = racerRepository.findById(racerId);
			Racer racer;

			if (_racer.isPresent()) {
				racer = new Racer(_racer.get());

				// Verify parameters and only update parameters that are present
				if (request.getFirstName().isPresent()) {
					racer.setFirstName(request.getFirstName().get());
				}
				if (request.getLastName().isPresent()) {
					racer.setLastName(request.getLastName().get());
				}
				if (request.getCategory().isPresent()) {
					racer.setCategory(request.getCategory().get());
				}

				return racer;
			} else {
				throw new Exception("Exception temp string change me");
			}
		} catch (Exception e) {
			throw new UpdateException(String.format("Unable to update rider with name %s %s", request.getFirstName(), request.getLastName()));
		}
	}
}
