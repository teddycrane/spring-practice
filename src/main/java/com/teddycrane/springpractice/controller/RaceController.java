package com.teddycrane.springpractice.controller;

import com.teddycrane.springpractice.EnumHelpers;
import com.teddycrane.springpractice.entity.Race;
import com.teddycrane.springpractice.entity.Racer;
import com.teddycrane.springpractice.exceptions.BadRequestException;
import com.teddycrane.springpractice.exceptions.BaseNotFoundException;
import com.teddycrane.springpractice.exceptions.RaceNotFoundException;
import com.teddycrane.springpractice.models.AddRacerRequest;
import com.teddycrane.springpractice.models.CreateRaceRequest;
import com.teddycrane.springpractice.repository.RaceRepository;
import com.teddycrane.springpractice.repository.RacerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(path="/race")
public class RaceController {

	@Autowired
	private RacerRepository racers;

	@Autowired
	private RaceRepository races;

	@GetMapping
	public @ResponseBody Race getRace(@RequestParam String id) throws RaceNotFoundException, BadRequestException {
		try {
			UUID raceId = UUID.fromString(id);
			System.out.printf("request id %s%n", id);
			Optional<Race> result = races.findById(raceId);
			System.out.printf("Found race %s%n", result.toString());

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

	@GetMapping(path="/all")
	public @ResponseBody List<Race> getAllRaces() throws RaceNotFoundException {
		Iterable<Race> response = races.findAll();
		List<Race> result = new ArrayList<>();
		response.forEach(result::add);

		if (result.size() > 0) {
			return result;
		} else {
			throw new RaceNotFoundException("Unable to find any races");
		}
	}

	@PutMapping
	public @ResponseBody Race createRace(@RequestBody CreateRaceRequest request) throws BadRequestException {
		try {
			Race r = new Race();
			r.setName(request.getName());
			r.setCategory(request.getCategory());
			races.save(r);
			return r;
		} catch (Exception e) {
			throw new BadRequestException(String.format("Unable to create race with name %s and category %s", request.getName(), EnumHelpers.getCategoryMapping(request.getCategory())));
		}
	}

	/**
	 * Adds a single racer
	 * @param request
	 * @return
	 * @throws BadRequestException
	 */
	@PatchMapping(path="/add-racer")
	public @ResponseBody Race addRacer(@RequestBody AddRacerRequest request) throws BadRequestException, BaseNotFoundException {
		try {
			Optional<Race> _race = races.findById(request.getRaceId());
			List<Racer> _racer = racers.findByFirstNameAndLastName(request.getFirstName(), request.getLastName());
			Race race;
			Racer racer;

			if (_race.isPresent()) {
				race = new Race(_race.get());
				if (_racer.size() > 0) {
					racer = new Racer(_racer.get(0));
				} else {
					throw new NoSuchElementException(String.format("No racer found with name %s %s", request.getFirstName(), request.getLastName()));
				}

				race.addRacer(racer);

				return race;
			} else {
				throw new NoSuchElementException(String.format("No race found with id %s", request.getRaceId().toString()));
			}
		} catch (NoSuchElementException e) {
			throw new BaseNotFoundException(e.getMessage());
		}
	}
}
