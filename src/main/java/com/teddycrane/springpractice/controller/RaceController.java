package com.teddycrane.springpractice.controller;

import com.teddycrane.springpractice.entity.Race;
import com.teddycrane.springpractice.entity.Racer;
import com.teddycrane.springpractice.exceptions.*;
import com.teddycrane.springpractice.models.AddRacerRequest;
import com.teddycrane.springpractice.models.CreateRaceRequest;
import com.teddycrane.springpractice.models.UpdateRaceRequest;
import com.teddycrane.springpractice.repository.RaceRepository;
import com.teddycrane.springpractice.repository.RacerRepository;
import com.teddycrane.springpractice.service.RaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(path = "/race")
public class RaceController {

	@Autowired
	private RacerRepository racerRepository;
	@Autowired
	private RaceRepository raceRepository;

	@Autowired
	private RaceService raceService;

	@GetMapping
	public @ResponseBody
	Race getRace(@RequestParam String id) throws RaceNotFoundException, BadRequestException {
		System.out.println("RaceController.getRace called");

		try {
			UUID uuid = UUID.fromString(id);
			return this.raceService.getRace(uuid);
		} catch (RaceNotFoundException e) {
			throw new RaceNotFoundException(String.format("Unable to find a race with id %s", id));
		} catch (IllegalArgumentException e) {
			System.out.println("Bad argument provided");
			throw new BadRequestException(String.format("The provided id %s was not in the correct format.  Please try again", id));
		}
	}

	@GetMapping(path = "/all")
	public @ResponseBody
	List<Race> getAllRaces() throws RaceNotFoundException {
		System.out.println("RaceController.getAllRaces called");
		return this.raceService.getAllRaces();
	}

	/**
	 * Creates a race if no race already exists with the same name
	 *
	 * @param request The request object with a required name and category
	 * @return The successfully created race
	 * @throws BadRequestException    Thrown if the input parameters do not allow for the creation of the race
	 * @throws DuplicateItemException Thrown if there is already a race in the database with the same name, case sensitive
	 */
	@PostMapping
	public @ResponseBody
	Race createRace(@RequestBody CreateRaceRequest request) throws BadRequestException, DuplicateItemException {
		System.out.println("RaceController.createRace called");
		try {
			if (request == null)
				throw new IllegalArgumentException("Request cannot be null");
			return this.raceService.createRace(request.getName(), request.getCategory());
		} catch (DuplicateItemException e) {
			throw new DuplicateItemException(e.getMessage());
		} catch (IllegalArgumentException e) {
			throw new BadRequestException(e.getMessage());
		}
	}

	@PatchMapping
	public @ResponseBody
	Race updateRace(@RequestBody UpdateRaceRequest request, @RequestParam String id) throws BadRequestException {
		System.out.println("RaceController.updateRace called");

		try {
			UUID raceId = UUID.fromString(id);
			if (request.getName().isPresent() || request.getCategory().isPresent()) {
				return this.raceService.updateRace(raceId, request.getName().get(), request.getCategory().get());
			} else {
				System.out.println("No valid parameters");
				throw new BadRequestException("No valid parameters provided!");
			}
		} catch (IllegalArgumentException e) {
			throw new BadRequestException(String.format("Unable to parse id %s", id));
		} catch (UpdateException e) {
			throw new UpdateException(e.getMessage());
		} catch (RaceNotFoundException e) {
			throw new RaceNotFoundException(e.getMessage());
		}
	}

	/**
	 * Adds a single racer
	 *
	 * @param request A request object with the racer's id and the race ID to add the racer to
	 * @return The Race object with the new racer
	 * @throws BadRequestException Thrown if the racer is unable to be added to the Race object
	 */
	@PatchMapping(path = "/add-racer")
	public @ResponseBody
	Race addRacer(@RequestBody AddRacerRequest request) throws BadRequestException, BaseNotFoundException {
		System.out.print("addRacer called\n");
		try {
			Optional<Race> _race = raceRepository.findById(request.getRaceId());
			Iterable<Racer> racers = racerRepository.findAllById(request.getRacerIds());
			Race race;

			if (_race.isPresent()) {
				race = new Race(_race.get());

				// add racers to race
				racers.forEach(race::addRacer);

				return raceRepository.save(race);
			} else {
				throw new BadRequestException("Unable to fulfill the request");
			}
		} catch (NoSuchElementException e) {
			System.out.printf("An error occurred! Error mesage follows: \n%s", e.getMessage());
			throw new BaseNotFoundException("Unable to find one of the specified racers!");
		}
	}
}
