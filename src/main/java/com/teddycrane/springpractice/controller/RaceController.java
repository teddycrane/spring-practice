package com.teddycrane.springpractice.controller;

import com.teddycrane.springpractice.entity.Race;
import com.teddycrane.springpractice.exceptions.BadRequestException;
import com.teddycrane.springpractice.exceptions.RaceNotFoundException;
import com.teddycrane.springpractice.repository.RaceRepository;
import com.teddycrane.springpractice.repository.RacerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

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
			Race race;
			Optional<Race> result = races.findById(raceId);

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
}
