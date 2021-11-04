package com.teddycrane.springpractice.controller;

import com.teddycrane.springpractice.entity.Race;
import com.teddycrane.springpractice.entity.Racer;
import com.teddycrane.springpractice.exceptions.BadRequestException;
import com.teddycrane.springpractice.exceptions.BaseNotFoundException;
import com.teddycrane.springpractice.exceptions.RaceNotFoundException;
import com.teddycrane.springpractice.helper.EnumHelpers;
import com.teddycrane.springpractice.models.AddRacerRequest;
import com.teddycrane.springpractice.models.CreateRaceRequest;
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

	@PutMapping
	public @ResponseBody
	Race createRace(@RequestBody CreateRaceRequest request) throws BadRequestException {
		try {
			Race r = new Race();
			r.setName(request.getName());
			r.setCategory(request.getCategory());
			raceRepository.save(r);
			return r;
		} catch (Exception e) {
			throw new BadRequestException(String.format("Unable to create race with name %s and category %s", request.getName(), EnumHelpers.getCategoryMapping(request.getCategory())));
		}
	}

	/**
	 * Adds a single racer
	 *
	 * @param request
	 * @return
	 * @throws BadRequestException
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

				raceRepository.save(race);
				return race;
			} else {
				throw new BadRequestException("Unable to fulfill the request");
			}
		} catch (NoSuchElementException e) {
			System.out.printf("An error occurred! Error mesage follows: \n%s", e.getMessage());
			throw new BaseNotFoundException("Unable to find one of the specified racers!");
		}
	}
}
