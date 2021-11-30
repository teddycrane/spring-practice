package com.teddycrane.springpractice.controller;

import com.teddycrane.springpractice.entity.Racer;
import com.teddycrane.springpractice.enums.FilterType;
import com.teddycrane.springpractice.exceptions.BadRequestException;
import com.teddycrane.springpractice.exceptions.RacerNotFoundException;
import com.teddycrane.springpractice.exceptions.UpdateException;
import com.teddycrane.springpractice.models.CreateRacerRequest;
import com.teddycrane.springpractice.models.UpdateRacerRequest;
import com.teddycrane.springpractice.service.IRacerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping(path = "/racer")
public class RacerController implements IRacerController
{

	private final Logger logger;

	private final IRacerService racerService;

	public RacerController(IRacerService racerService)
	{
		this.logger = LogManager.getLogger(this.getClass());
		this.racerService = racerService;
	}

	@Override
	public List<Racer> getAllRacers(boolean isDeleted)
	{
		this.logger.trace("RacerController.getAllRaces called");
		if (isDeleted)
		{
			return racerService.getAllRacersWithDeleted();
		} else
		{
			return racerService.getAllRacers();
		}
	}

	@Override
	public Racer getRacer(String id) throws RacerNotFoundException
	{
		this.logger.trace("RacerController.getRacer called");
		try
		{

			UUID uuid = UUID.fromString(id);
			return this.racerService.getRacerById(uuid);

		} catch (RacerNotFoundException e)
		{
			logger.error("No racer found!", e);
			throw new RacerNotFoundException(String.format("No racer found with id %s", id));
		}
	}

	@Override
	public Racer addRacer(CreateRacerRequest request) throws BadRequestException
	{
		this.logger.trace("RacerController.addRacer called");

		try
		{
			// verify required parameters
			return this.racerService.addRacer(request.getFirstName(), request.getLastName());
		} catch (Exception e)
		{
			throw new BadRequestException(String.format("Unable to create a racer with name %s %s", request.getFirstName(), request.getLastName()));
		}
	}

	/**
	 * Handles PATCH requests to /racer/update?id=racerId
	 *
	 * @param request The request object containing the fields to update
	 * @return The updated Racer object
	 * @throws UpdateException Thrown if the racer does not exist, or if the racer fails to update
	 */
	@Override
	public Racer updateRacer(UpdateRacerRequest request, String id) throws RacerNotFoundException, BadRequestException
	{
		this.logger.trace("RacerController.updateRacer called");
		try
		{
			UUID uuid = UUID.fromString(id);

			// validate that at least one of the parameters are not empty or null

			return this.racerService.updateRacer(uuid,
					request.getFirstName().isPresent() ? request.getFirstName().get() : null,
					request.getLastName().isPresent() ? request.getLastName().get() : null,
					request.getCategory().isPresent() ? request.getCategory().get() : null);
		} catch (BadRequestException e)
		{
			logger.error("Bad request", e);
			throw new BadRequestException(e.getMessage());
		} catch (RacerNotFoundException e)
		{
			logger.error("No racer found!", e);
			throw new RacerNotFoundException(String.format("No racer found with id %s.", id));
		} catch (IllegalArgumentException e)
		{
			logger.error("Unable to parse the provided id", e);
			throw new BadRequestException(String.format("Unable to parse the id %s. Please try again", id));
		}
	}

	@Override
	public Racer deleteRacer(String id) throws RacerNotFoundException
	{
		this.logger.trace("RacerController.deleteRacer called");

		try
		{
			UUID uuid = UUID.fromString(id);
			return this.racerService.deleteRacer(uuid);
		} catch (NoSuchElementException e)
		{
			logger.error("No element found with the provided id", e);
			throw new RacerNotFoundException(e.getMessage());
		}
	}

	@Override
	public Racer restoreRacer(String id) throws RacerNotFoundException
	{
		this.logger.trace("RacerController.restoreRacer called");

		try
		{
			UUID uuid = UUID.fromString(id);
			return this.racerService.restoreRacer(uuid);
		} catch (RacerNotFoundException e)
		{
			logger.error("Unable to find the specified racer", e);
			throw new RacerNotFoundException(e.getMessage());
		}
	}

	/**
	 * Handles requests to return a list of racers by the provided type.
	 *
	 * @param type The String value of the FilterType enum to use for the search
	 * @return A List of Racers matching the filter type
	 * @throws BadRequestException Throws if there is not a filter type provided, or if the filter type is invalid.
	 */
	@Override
	public List<Racer> getRacersByType(String type) throws BadRequestException
	{
		this.logger.trace("getRacersByTypeCalled");

		if (type == null)
		{
			this.logger.error("No type provided!");
			throw new BadRequestException("No filter type provided!");
		}

		// set of enum values
		EnumSet<FilterType> rawValues = EnumSet.allOf(FilterType.class);
		Set<String> enumValues = new HashSet<>();
		rawValues.forEach((element) -> enumValues.add(element.toString()));

		// validate that the query param is a valid enum type (non case-sensitive)
		if (enumValues.contains(type.toLowerCase()))
		{

		}
	}
}
