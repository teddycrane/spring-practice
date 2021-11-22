package com.teddycrane.springpractice.controller;

import com.teddycrane.springpractice.entity.Race;
import com.teddycrane.springpractice.exceptions.*;
import com.teddycrane.springpractice.models.AddRacerRequest;
import com.teddycrane.springpractice.models.CreateRaceRequest;
import com.teddycrane.springpractice.models.UpdateRaceRequest;
import com.teddycrane.springpractice.service.IRaceService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/race")
public class RaceController implements IRaceController
{

	private final Logger logger;

	private final IRaceService raceService;

	public RaceController(IRaceService raceService)
	{
		this.logger = LogManager.getLogger(this.getClass());
		this.raceService = raceService;
	}

	@Override
	public Race getRace(String id) throws RaceNotFoundException, BadRequestException
	{
		this.logger.info("RaceController.getRace called");

		try
		{
			UUID uuid = UUID.fromString(id);
			return this.raceService.getRace(uuid);
		} catch (RaceNotFoundException e)
		{
			this.logger.error("Unable to find race!", e);
			throw new RaceNotFoundException(String.format("Unable to find a race with id %s", id));
		} catch (IllegalArgumentException e)
		{
			this.logger.error("Id was not formatted correctly.", e);
			throw new BadRequestException(String.format("The provided id %s was not in the correct format.  Please try again", id));
		}
	}

	@Override
	public List<Race> getAllRaces() throws RaceNotFoundException
	{
		this.logger.info("RaceController.getAllRaces called");
		return this.raceService.getAllRaces();
	}

	/**
	 * Creates a race if no race already exists with the same name
	 *
	 * @param request The request object with a required name and category
	 * @return The successfully created race
	 * @throws BadRequestException    Thrown if the input parameters do not allow for the creation of the race
	 * @throws DuplicateItemException Thrown if there is already a race in the database with the same name, case sensitive
	 */
	@Override
	public Race createRace(CreateRaceRequest request) throws BadRequestException, DuplicateItemException
	{
		this.logger.info("RaceController.createRace called");
		try
		{
			if (request == null)
				throw new IllegalArgumentException("Request cannot be null");
			return this.raceService.createRace(request.getName(), request.getCategory());
		} catch (DuplicateItemException e)
		{
			this.logger.error("Unable to create a duplicate item", e);
			throw new DuplicateItemException(e.getMessage());
		} catch (IllegalArgumentException e)
		{
			this.logger.error("Bad request", e);
			throw new BadRequestException(e.getMessage());
		}
	}

	@Override
	public Race updateRace(UpdateRaceRequest request, String id) throws BadRequestException, DuplicateItemException, RaceNotFoundException
	{
		this.logger.info("RaceController.updateRace called");

		try
		{
			UUID raceId = UUID.fromString(id);
			if (request.getName().isPresent() || request.getCategory().isPresent())
			{
				return this.raceService.updateRace(raceId, request.getName().get(), request.getCategory().get());
			} else
			{
				this.logger.error("No valid parameters");
				throw new BadRequestException("No valid parameters provided!");
			}
		} catch (IllegalArgumentException e)
		{
			this.logger.error("Unable to parse the provided id", e);
			throw new BadRequestException(String.format("Unable to parse id %s", id));
		} catch (UpdateException e)
		{
			this.logger.error("Unable to update the specified race!", e);
			throw new UpdateException(e.getMessage());
		} catch (RaceNotFoundException e)
		{
			this.logger.error("Unable to find the specified race!", e);
			throw new RaceNotFoundException(e.getMessage());
		} catch (DuplicateItemException e)
		{
			this.logger.error("Unable to have duplicate entrants in a race!", e);
			throw new DuplicateItemException(e.getMessage());
		}
	}

	/**
	 * Adds a single racer
	 *
	 * @param request A request object with the racer's id and the race ID to add the racer to
	 * @return The Race object with the new racer
	 * @throws BadRequestException Thrown if the racer is unable to be added to the Race object
	 */
	@Override
	public Race addRacer(AddRacerRequest request, String raceId) throws BadRequestException, RaceNotFoundException, RacerNotFoundException
	{
		this.logger.info("RaceController.addRacer called");

		try
		{
			UUID id = UUID.fromString(raceId);
			if (request != null)
			{
				return this.raceService.addRacer(id, request.getRacerIds());
			} else
			{
				this.logger.error("Request body is not valid");
				throw new BadRequestException("Request body cannot be null!");
			}
		} catch (RacerNotFoundException e)
		{
			this.logger.error("Unable to find a racer!", e);
			throw new RacerNotFoundException(e.getMessage());
		} catch (RaceNotFoundException e)
		{
			this.logger.error("Unable to find the specified race!", e);
			throw new RaceNotFoundException(e.getMessage());
		} catch (IllegalArgumentException e)
		{
			String message = String.format("Unable to parse the provided id %s", raceId);
			this.logger.error(message, e);
			throw new BadRequestException(message);
		}
	}
}
