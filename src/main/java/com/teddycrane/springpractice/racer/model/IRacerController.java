package com.teddycrane.springpractice.racer.model;

import com.teddycrane.springpractice.error.BadRequestException;
import com.teddycrane.springpractice.error.RacerNotFoundException;
import com.teddycrane.springpractice.racer.request.CreateRacerRequest;
import com.teddycrane.springpractice.racer.Racer;
import com.teddycrane.springpractice.racer.request.UpdateRacerRequest;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
	List<Racer> getRacersByType(@RequestParam String type, @RequestParam String value) throws BadRequestException;

}
