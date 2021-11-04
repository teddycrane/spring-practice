package com.teddycrane.springpractice.controller;

import com.teddycrane.springpractice.entity.Race;
import com.teddycrane.springpractice.entity.Racer;
import com.teddycrane.springpractice.exceptions.BadRequestException;
import com.teddycrane.springpractice.exceptions.BaseNotFoundException;
import com.teddycrane.springpractice.exceptions.DuplicateItemException;
import com.teddycrane.springpractice.exceptions.RaceNotFoundException;
import com.teddycrane.springpractice.helper.EnumHelpers;
import com.teddycrane.springpractice.models.AddRacerRequest;
import com.teddycrane.springpractice.models.CreateRaceRequest;
import com.teddycrane.springpractice.models.UpdateRaceRequest;
import com.teddycrane.springpractice.repository.RaceRepository;
import com.teddycrane.springpractice.repository.RacerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(path = "/race")
public class RaceController {

	@Autowired
	private RacerRepository racerRepository;
	@Autowired
	private RaceRepository raceRepository;

	@GetMapping
	public @ResponseBody
	Race getRace(@RequestParam String id) throws RaceNotFoundException, BadRequestException {
		try {
			UUID raceId = UUID.fromString(id);
			System.out.printf("request id %s%n", id);
			Optional<Race> result = raceRepository.findById(raceId);
			System.out.printf("Found race %s%n", result);

			if (result.isPresent()) {
				return new Race(result.get());
			} else {
				throw new NoSuchElementException(String.format("No element with id %s", id));
			}
		} catch (NoSuchElementException e) {
			throw new RaceNotFoundException(e.getMessage());
		} catch (Exception e) {
			throw new BadRequestException("Bad Request");
		}
	}

	@GetMapping(path = "/all")
	public @ResponseBody
	List<Race> getAllRaces() throws RaceNotFoundException {
		Iterable<Race> response = raceRepository.findAll();
		List<Race> result = new ArrayList<>();
		response.forEach(result::add);

		if (result.size() > 0) {
			return result;
		} else {
			throw new RaceNotFoundException("Unable to find any races");
		}
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
		System.out.println("createRace called");
		try {
			Optional<Race> existing = raceRepository.findByName(request.getName());

			if (existing.isPresent()) {
				throw new DuplicateItemException(String.format("A race with the name %s already exists!", request.getName()));
			}

			Race r = new Race();
			r.setName(request.getName());
			r.setCategory(request.getCategory());
			return raceRepository.save(r);
		} catch (Exception e) {
			throw new BadRequestException(String.format("Unable to create race with name %s and category %s", request.getName(), EnumHelpers.getCategoryMapping(request.getCategory())));
		}
	}

	@PatchMapping
	public @ResponseBody
	Race updateRace(@RequestBody UpdateRaceRequest request, @RequestParam String id) throws BaseNotFoundException {
		System.out.println("updateRace called");

		try {
			UUID raceId = UUID.fromString(id);
			Optional<Race> race = raceRepository.findById(raceId);
			Race r;

			if (race.isPresent()) {
				r = new Race(race.get());

				// request optional param validation
				if (request.getName().isPresent()) {
					r.setName(request.getName().get());
				}
				if (request.getCategory().isPresent()) {
					r.setCategory(request.getCategory().get());
				}

				return raceRepository.save(r);
			} else {
				throw new BaseNotFoundException(String.format("No element found with the id %s", id));
			}
		} catch (IllegalArgumentException e) {
			throw new BadRequestException(String.format("Unable to parse id %s", id));
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
