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

import java.util.*;

@Controller
@RequestMapping(path = "/racer")
public class RacerController {

	@Autowired
	private RacerRepository racerRepository;

	@GetMapping(path = "/all")
	public @ResponseBody
	List<Racer> getAllRacers() {
		System.out.println("getAllRacers called");
		List<Racer> allRacers = new ArrayList<>();
		racerRepository.findAll().forEach(allRacers::add);

		allRacers.removeIf(Racer::getIsDeleted);
		return allRacers;
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

		try {
			racerRepository.save(r);
			return r;
		} catch (IndexOutOfBoundsException e) {
			System.out.printf("An error occurred! Stack Trace: %s\n", e.getMessage());
			throw new CreationException(String.format("Unable to create a racer with name %s %s", request.getFirstName(), request.getLastName()));
		}
	}

	/**
	 * Handles PATCH requests to /racer/update?id=racerId
	 *
	 * @param request The request object containing the fields to update
	 * @param id      The id of the racer to update
	 * @return The updated Racer object
	 * @throws UpdateException Thrown if the racer does not exist, or if the racer fails to update
	 */
	@PatchMapping(path = "/update")
	public @ResponseBody
	Racer updateRacer(@RequestBody @NotNull UpdateRacerRequest request, @RequestParam String id) throws UpdateException {
		try {
			System.out.println("updateRacer called");
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

	@DeleteMapping
	public @ResponseBody
	Racer deleteRacer(@RequestParam String id) throws RacerNotFoundException {
		System.out.printf("deleteRacer called for id %s\n", id);
		try {
			UUID racerId = UUID.fromString(id);
			Optional<Racer> _racer = racerRepository.findById(racerId);
			Racer racer;

			if (_racer.isPresent()) {
				racer = _racer.get();
				racerRepository.delete(racer);
				return racer;
			} else {
				throw new NoSuchElementException("No element found with the given id");
			}
		} catch (NoSuchElementException e) {
			System.out.println("No element found");
			throw new RacerNotFoundException(String.format("Unable to find a rider with id %s", id));
		} catch (Exception e) {
			System.out.println("an error occurred");
			throw new RacerNotFoundException("No racer found");
		}
	}
}
