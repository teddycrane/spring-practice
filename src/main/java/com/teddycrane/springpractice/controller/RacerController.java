package com.teddycrane.springpractice.controller;

import com.teddycrane.springpractice.entity.Racer;
import com.teddycrane.springpractice.exceptions.BadRequestException;
import com.teddycrane.springpractice.exceptions.RacerNotFoundException;
import com.teddycrane.springpractice.exceptions.UpdateException;
import com.teddycrane.springpractice.models.CreateRacerRequest;
import com.teddycrane.springpractice.models.UpdateRacerRequest;
import com.teddycrane.springpractice.repository.RacerRepository;
import com.teddycrane.springpractice.service.RacerService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Controller
@RequestMapping(path = "/racer")
public class RacerController {

	@Autowired
	private RacerService racerService;

	@Autowired
	private RacerRepository racerRepository;

	@GetMapping(path = "/all")
	public @ResponseBody
	List<Racer> getAllRacers(@RequestParam(required = false) boolean isDeleted) {
		System.out.println("RacerController.getAllRacers called");
		if (isDeleted) {
			System.out.println("Returning all racers, including deleted entries");
			return racerService.getAllRacersWithDeleted();
		} else {
			System.out.println("Filtering out deleted entries");
			return racerService.getAllRacers();
		}
	}

	@GetMapping
	public @ResponseBody
	Racer getRacer(@RequestParam String id) throws RacerNotFoundException {
		try {
			System.out.printf("RacerController.getRacer called with id %s", id);

			UUID uuid = UUID.fromString(id);
			return this.racerService.getRacerById(uuid);

		} catch (RacerNotFoundException e) {
			System.out.println("Unable to find racer");
			throw new RacerNotFoundException(String.format("No racer found with id %s", id));
		}
	}

	@PostMapping(path = "/new")
	public @ResponseBody
	Racer addRacer(@RequestBody @NotNull CreateRacerRequest request) throws BadRequestException {
		System.out.println("RacerController.addRacer called");

		try {
			// verify required parameters
			return this.racerService.addRacer(request.getFirstName(), request.getLastName());
		} catch (Exception e) {
			throw new BadRequestException(String.format("Unable to create a racer with name %s %s", request.getFirstName(), request.getLastName()));
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
	Racer updateRacer(@RequestBody @NotNull UpdateRacerRequest request, @RequestParam String id) throws UpdateException, BadRequestException {
		try {
			System.out.println("updateRacer called");
			UUID uuid = UUID.fromString(id);

			// verify parameters - if any of the required parameters are valid, continue, otherwise, throw a bad request error
			if (request.getFirstName().isPresent() || request.getLastName().isPresent() || request.getCategory().isPresent()) {
				return this.racerService.updateRacer(uuid, request.getFirstName().get(), request.getLastName().get(), request.getCategory().get());
			} else {
				throw new BadRequestException("No update parameters specified!");
			}
		} catch (BadRequestException e) {
			System.out.print("error");
			throw new BadRequestException(e.getMessage());
		} catch (RacerNotFoundException e) {
			System.out.println("No racer found!");
			throw new UpdateException(String.format("No racer found with id %s.", id));
		}
	}

	@DeleteMapping
	public @ResponseBody
	Racer deleteRacer(@RequestParam String id) throws RacerNotFoundException {
		System.out.println("RacerController.deleteRacer called");

		try {
			UUID uuid = UUID.fromString(id);
			return this.racerService.deleteRacer(uuid);
		} catch (NoSuchElementException e) {
			String message = String.format("No element found with id %s\n", id);
			System.out.println(message);
			throw new RacerNotFoundException(message);
		}
	}
}
