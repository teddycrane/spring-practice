package com.teddycrane.springpractice.controller.model;

import com.teddycrane.springpractice.entity.Race;
import com.teddycrane.springpractice.exceptions.*;
import com.teddycrane.springpractice.models.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
	Race updateRace(@RequestBody @Valid UpdateRaceRequest request, @RequestParam String id) throws BadRequestException, RaceNotFoundException, UpdateException;

	@PatchMapping(path = "/add-racer")
	Race addRacer(@RequestBody @Valid AddRacerRequest request, @RequestParam String raceId) throws BadRequestException, RaceNotFoundException, RacerNotFoundException;

	@PostMapping(path = "/start-race")
	Race startRace(@RequestParam String raceId) throws RaceNotFoundException, BadRequestException, StartException;

	@PostMapping(path = "/end-race")
	Race endRace(@RequestParam String raceId) throws RaceNotFoundException, BadRequestException;

	@PostMapping(path = "/place")
	Race setRacerResult(@RequestParam String raceId, @RequestBody @Valid SetResultRequest request) throws RaceNotFoundException, RacerNotFoundException, DuplicateItemException;

	@GetMapping(path = "/results")
	RaceResult getResults(@RequestParam String raceId) throws RaceNotFoundException, BadRequestException;

	@DeleteMapping
	Race deleteRace(@RequestParam String raceId) throws BadRequestException, RaceNotFoundException;

	@GetMapping(path = "/racer-results")
	Map<UUID, Integer> getResultsForRacer(@RequestParam String racerId) throws BadRequestException, RacerNotFoundException;
}
