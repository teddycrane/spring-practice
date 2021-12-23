package com.teddycrane.springpractice.race;

import com.teddycrane.springpractice.event.request.SetResultRequest;
import com.teddycrane.springpractice.models.BaseController;
import com.teddycrane.springpractice.enums.Category;
import com.teddycrane.springpractice.enums.RaceFilterType;
import com.teddycrane.springpractice.error.*;
import com.teddycrane.springpractice.helper.EnumHelpers;
import com.teddycrane.springpractice.models.*;
import com.teddycrane.springpractice.race.model.IRaceController;
import com.teddycrane.springpractice.race.model.IRaceService;
import com.teddycrane.springpractice.race.model.RaceResult;
import com.teddycrane.springpractice.race.request.CreateRaceRequest;
import com.teddycrane.springpractice.race.request.UpdateRaceRequest;
import com.teddycrane.springpractice.racer.request.AddRacerRequest;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(path = "/race")
public class RaceController extends BaseController implements IRaceController
{

	private final IRaceService raceService;

	public RaceController(IRaceService raceService)
	{
		super();
		this.raceService = raceService;
	}

	@Override
	public Race getRace(String id) throws RaceNotFoundException, BadRequestException
	{
		this.logger.trace("getRace called");

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
		this.logger.trace("getAllRaces called");
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
		this.logger.trace("createRace called");
		if (request == null)
		{
			logger.error("No request body provided!");
			throw new BadRequestException("The request body cannot be empty");
		}

		try
		{
			return this.raceService.createRace(request.getName(), request.getCategory());
		} catch (DuplicateItemException e)
		{
			this.logger.error("Unable to create a duplicate item", e);
			throw new DuplicateItemException(e.getMessage());
		}
	}

	@Override
	public Race updateRace(UpdateRaceRequest request, String id) throws BadRequestException, RaceNotFoundException, UpdateException
	{
		logger.trace("updateRace called");
		try
		{
			UUID raceId = UUID.fromString(id);

			// verify requred parameters
			if (request.getName().isPresent() || request.getCategory().isPresent())
			{
				return this.raceService.updateRace(raceId, request.getName().get(), request.getCategory().get());
			} else
			{
				this.logger.error("No valid parameters in the request {}", request);
				throw new BadRequestException("No valid parameters provided!");
			}
		} catch (IllegalArgumentException e)
		{
			this.logger.error("Unable to parse the provided id {}", id);
			throw new BadRequestException(String.format("Unable to parse id %s", id));
		} catch (UpdateException e)
		{
			this.logger.error("Unable to update the specified race {}", id);
			throw new UpdateException(e.getMessage());
		} catch (RaceNotFoundException e)
		{
			this.logger.error("Unable to find a race with id {}", id);
			throw new RaceNotFoundException(e.getMessage());
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
	public Race addRacer(AddRacerRequest request, String raceId) throws BadRequestException, RaceNotFoundException, RacerNotFoundException, UpdateException
	{
		this.logger.trace("addRacer called");

		try
		{
			// validate that request is in fact, not null
			UUID id = UUID.fromString(raceId);
			return this.raceService.addRacer(id, request.getRacerIds());
		} catch (RacerNotFoundException e)
		{
			throw new RacerNotFoundException(e.getMessage());
		} catch (RaceNotFoundException e)
		{
			throw new RaceNotFoundException(e.getMessage());
		} catch (IllegalArgumentException e)
		{
			logger.error("Unable to parse the id {}", raceId);
			throw new BadRequestException(String.format("Unable to parse the provided id %s", raceId));
		}
	}

	@Override
	public Race startRace(String raceId) throws RaceNotFoundException, BadRequestException, StartException
	{
		this.logger.trace("startRace called");

		try
		{
			UUID id = UUID.fromString(raceId);
			return this.raceService.startRace(id);
		} catch (IllegalArgumentException e)
		{
			this.logger.error("Unable to parse id!", e);
			throw new BadRequestException(e.getMessage());
		} catch (RaceNotFoundException e)
		{
			throw new RaceNotFoundException(e.getMessage());
		} catch (StartException e)
		{
			throw new StartException(e.getMessage());
		}
	}

	@Override
	public Race endRace(String raceId) throws RaceNotFoundException, BadRequestException
	{
		this.logger.trace("endRace called");

		try
		{
			UUID id = UUID.fromString(raceId);
			return this.raceService.endRace(id);
		} catch (RaceNotFoundException e)
		{
			throw new RaceNotFoundException(e.getMessage());
		} catch (EndException e)
		{
			String message = String.format("Unable to parse id %s", raceId);
			this.logger.error(message);
			throw new BadRequestException(message);
		}
	}

	@Override
	public Race setRacerResult(String raceId, SetResultRequest request) throws RaceNotFoundException, RacerNotFoundException, DuplicateItemException
	{
		this.logger.trace("setRacerResult called");

		try
		{
			UUID mappedRaceId = UUID.fromString(raceId);
			ArrayList<UUID> mappedIds = new ArrayList<>();
			String[] requestIds = request.getIds();

			for (String id : requestIds)
				mappedIds.add(UUID.fromString(id));

			return this.raceService.placeRacersInFinishOrder(mappedRaceId, mappedIds);
		} catch (IllegalArgumentException e)
		{
			this.logger.error("Unable to parse one of the ids  raceId: {}, racerId: {}", raceId, Arrays.toString(request.getIds()));
			throw new BadRequestException(String.format("Unable to parse one of the ids  raceId: %s, racerId: %s", raceId, Arrays.toString(request.getIds())));
		}
	}

	@Override
	public RaceResult getResults(String raceId) throws RaceNotFoundException, BadRequestException
	{
		this.logger.trace("getResults called");

		try
		{
			UUID id = UUID.fromString(raceId);
			return this.raceService.getResults(id);
		} catch (IllegalArgumentException e)
		{
			this.logger.error("Unable to parse the provided id {}", raceId);
			throw new BadRequestException("Unable to parse the provided id!");
		} catch (RaceNotFoundException e)
		{
			throw new RaceNotFoundException(e.getMessage());
		}
	}

	@Override
	public Race deleteRace(String raceId) throws BadRequestException, RaceNotFoundException
	{
		this.logger.trace("deleteRace called");

		try
		{
			UUID id = UUID.fromString(raceId);
			return this.raceService.deleteRace(id);
		} catch (RaceNotFoundException e)
		{
			throw new RaceNotFoundException(e.getMessage());
		} catch (IllegalArgumentException e)
		{
			this.logger.error("Unable to parse the id {}", raceId);
			throw new BadRequestException(e.getMessage());
		}
	}

	@Override
	public Map<UUID, Integer> getResultsForRacer(String id) throws BadRequestException, RacerNotFoundException
	{
		logger.trace("getResultsForRacer called");

		try
		{
			UUID racerId = UUID.fromString(id);
			return this.raceService.getResultsForRacer(racerId);
		} catch (IllegalArgumentException e)
		{
			logger.error("Unable to parse the id {}", id);
			throw new BadRequestException("Unable to parse the provided id");
		} catch (RacerNotFoundException e)
		{
			throw new RacerNotFoundException(e.getMessage());
		}
	}

	@Override
	public Collection<Race> filterRaces(String type, String value) throws BadRequestException
	{
		logger.trace("filterRaces called");

		// validate if type is a valid race filter type
		if (EnumHelpers.testEnumValue(RaceFilterType.class, type))
		{
			try
			{
				RaceFilterType filterType = RaceFilterType.valueOf(type.toUpperCase());
				boolean isValidCategory = EnumHelpers.testEnumValue(Category.class, value);

				// validate category, if provided
				if (filterType == RaceFilterType.CATEGORY && isValidCategory)
				{
					return this.raceService.filterRace(filterType, Either.right(Category.valueOf(value.toUpperCase())));
				} else if (filterType == RaceFilterType.CATEGORY)
				{
					// if the type is category but it's invalid
					logger.error("{} is not a valid category!", value);
					throw new BadRequestException("The provided value is not a valid category");
				} else if (filterType == RaceFilterType.NAME)
				{
					return this.raceService.filterRace(filterType, Either.left(value));
				} else
				{
					logger.error("Unknown error occurred!");
					throw new InternalServerError("An unknown error occurred!");
				}

			} catch (IllegalArgumentException e)
			{
				logger.error("{} is an invalid filter type", type);
				throw new BadRequestException("Invalid filter type provided!");
			}
		} else
		{
			logger.error("{} is not a valid filter type", type);
			throw new BadRequestException("The provided filter type is not a valid filter type");
		}
	}
}
