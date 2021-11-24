package com.teddycrane.springpractice.controller;

import com.teddycrane.springpractice.entity.Race;
import com.teddycrane.springpractice.exceptions.*;
import com.teddycrane.springpractice.models.AddRacerRequest;
import com.teddycrane.springpractice.models.CreateRaceRequest;
import com.teddycrane.springpractice.models.UpdateRaceRequest;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/race")
public interface IRaceController
{

	@GetMapping
	Race getRace(@RequestParam String id) throws RaceNotFoundException, BadRequestException;

	@GetMapping(path = "/all")
	List<Race> getAllRaces() throws RaceNotFoundException;

	@PostMapping
	Race createRace(@RequestBody @Valid CreateRaceRequest request) throws BadRequestException, DuplicateItemException;

	@PatchMapping
	Race updateRace(@RequestBody @Valid UpdateRaceRequest request, @RequestParam String id) throws BadRequestException, DuplicateItemException;

	@PatchMapping(path = "/add-racer")
	Race addRacer(@RequestBody @Valid AddRacerRequest request, @RequestParam String raceId) throws BadRequestException, RaceNotFoundException, RacerNotFoundException;

	@PostMapping(path = "/start-race")
	Race startRace(@RequestParam String raceId) throws RaceNotFoundException, BadRequestException, StartException;

	@PostMapping(path = "/end-race")
	Race endRace(@RequestParam String raceId) throws RaceNotFoundException, BadRequestException;
}
