package com.teddycrane.springpractice.controller;

import com.teddycrane.springpractice.controller.model.IRacerController;
import com.teddycrane.springpractice.entity.Racer;
import com.teddycrane.springpractice.enums.Category;
import com.teddycrane.springpractice.enums.FilterType;
import com.teddycrane.springpractice.exceptions.BadRequestException;
import com.teddycrane.springpractice.exceptions.RacerNotFoundException;
import com.teddycrane.springpractice.exceptions.UpdateException;
import com.teddycrane.springpractice.helper.EnumHelpers;
import com.teddycrane.springpractice.models.CreateRacerRequest;
import com.teddycrane.springpractice.models.UpdateRacerRequest;
import com.teddycrane.springpractice.service.model.IRacerService;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(path = "/racer")
public class RacerController extends BaseController implements IRacerController
{

	private final IRacerService racerService;

	public RacerController(IRacerService racerService)
	{
		super();
		this.racerService = racerService;
	}

	@Override
	public List<Racer> getAllRacers(boolean isDeleted)
	{
		this.logger.trace("getAllRaces called");
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
		this.logger.trace("getRacer called");
		try
		{

			UUID uuid = UUID.fromString(id);
			return this.racerService.getRacerById(uuid);

		} catch (RacerNotFoundException e)
		{
			logger.error("No racer found for the given id {}", id);
			throw new RacerNotFoundException(String.format("No racer found with id %s", id));
		}
	}

	@Override
	public Racer addRacer(CreateRacerRequest request) throws BadRequestException
	{
		this.logger.trace("addRacer called");

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
		this.logger.trace("updateRacer called");
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
			throw new BadRequestException(e.getMessage());
		} catch (RacerNotFoundException e)
		{
			logger.error("No racer found with the id {}", id);
			throw new RacerNotFoundException(String.format("No racer found with id %s.", id));
		} catch (IllegalArgumentException e)
		{
			logger.error("Unable to parse the id {}", id);
			throw new BadRequestException(String.format("Unable to parse the id %s. Please try again", id));
		}
	}

	@Override
	public Racer deleteRacer(String id) throws RacerNotFoundException
	{
		this.logger.trace("deleteRacer called");

		try
		{
			UUID uuid = UUID.fromString(id);
			return this.racerService.deleteRacer(uuid);
		} catch (NoSuchElementException e)
		{
			logger.error("No element found with the id {}", id);
			throw new RacerNotFoundException(e.getMessage());
		}
	}

	@Override
	public Racer restoreRacer(String id) throws RacerNotFoundException
	{
		this.logger.trace("restoreRacer called");

		try
		{
			UUID uuid = UUID.fromString(id);
			return this.racerService.restoreRacer(uuid);
		} catch (RacerNotFoundException e)
		{
			logger.error("Unable to find a racer with id {}", id);
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
	public List<Racer> getRacersByType(String type, String value) throws BadRequestException
	{
		this.logger.trace("getRacersByType called");

		// validate that the query param is a valid enum type (non case-sensitive)
		if (EnumHelpers.testEnumValue(FilterType.class, type))
		{
			try
			{
				FilterType filterType = FilterType.valueOf(type.toUpperCase());

				// category validation
				if (filterType == FilterType.CATEGORY && !EnumHelpers.testEnumValue(Category.class, value))
				{
					this.logger.error("Unable to parse the category value {}", value);
					throw new BadRequestException("Unable to parse the provided category value!");
				}

				return this.racerService.getRacersByType(filterType, value.toUpperCase());
			} catch (BadRequestException e)
			{
				throw new BadRequestException(e.getMessage());
			}
		} else
		{
			this.logger.error("The filter type {} is not a valid filter type", type);
			throw new BadRequestException("The provided filter type is not a valid filter type");
		}
	}
}
