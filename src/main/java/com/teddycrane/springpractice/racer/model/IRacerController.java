package com.teddycrane.springpractice.racer.model;

import com.teddycrane.springpractice.error.BadRequestException;
import com.teddycrane.springpractice.error.RacerNotFoundException;
import com.teddycrane.springpractice.racer.request.CreateRacerRequest;
import com.teddycrane.springpractice.racer.Racer;
import com.teddycrane.springpractice.racer.request.UpdateRacerRequest;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import javax.validation.Valid;
import java.util.List;

@RequestMapping(path = "/racer")
public interface IRacerController {

	@Operation(summary = "Get all racers")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found racers", content = {
					@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Racer.class)))
			}),
	})
	@GetMapping(path = "/all")
	List<Racer> getAllRacers(
			@Parameter(description = "Boolean flag indicating if deleted entries should be included") @RequestParam(required = false) boolean isDeleted);

	@Operation(summary = "Get racer by ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found racer", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Racer.class))
			}),
			@ApiResponse(responseCode = "404", description = "No racers found for the specified ID", content = {
					@Content()
			})
	})
	@GetMapping("/{id}")
	Racer getRacer(@Parameter(description = "The object ID to find the racer associated with") @PathVariable String id)
			throws RacerNotFoundException;

	@Operation(summary = "Create new racer")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successfully created racer", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Racer.class)) }),
			@ApiResponse(responseCode = "400", description = "Unable to create racer")
	})
	@PostMapping(path = "/new")
	Racer addRacer(@Valid @RequestBody CreateRacerRequest request) throws BadRequestException;

	@PatchMapping(path = "/update/{id}")
	Racer updateRacer(@RequestBody UpdateRacerRequest request, @PathVariable String id)
			throws RacerNotFoundException, BadRequestException;

	@DeleteMapping(path = "/{id}")
	Racer deleteRacer(@PathVariable String id) throws RacerNotFoundException;

	@PatchMapping(path = "/restore/{id}")
	Racer restoreRacer(@PathVariable String id) throws RacerNotFoundException;

	@GetMapping(path = "/filter")
	List<Racer> getRacersByType(@RequestParam String type, @RequestParam String value) throws BadRequestException;

}
