package com.teddycrane.springpractice.controller;

import com.teddycrane.springpractice.entity.Racer;
import com.teddycrane.springpractice.exceptions.BadRequestException;
import com.teddycrane.springpractice.exceptions.RacerNotFoundException;
import com.teddycrane.springpractice.models.CreateRacerRequest;
import com.teddycrane.springpractice.models.UpdateRacerRequest;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/racer")
public interface IRacerController
{

	@GetMapping(path = "/all")
	List<Racer> getAllRacers(@RequestParam(required = false) boolean isDeleted);

	@GetMapping
	Racer getRacer(@RequestParam String id) throws RacerNotFoundException;

	@PostMapping(path = "/new")
	Racer addRacer(@Valid @RequestBody CreateRacerRequest request) throws BadRequestException;

	@PatchMapping(path = "/update")
	Racer updateRacer(@RequestBody UpdateRacerRequest request, @RequestParam String id) throws RacerNotFoundException, BadRequestException;

	@DeleteMapping
	Racer deleteRacer(@RequestParam String id) throws RacerNotFoundException;

	@PatchMapping(path = "/restore")
	Racer restoreRacer(@RequestParam String id) throws RacerNotFoundException;

	@GetMapping(path = "/filter")
	List<Racer> getRacersByType(@RequestParam String type) throws BadRequestException;
}
