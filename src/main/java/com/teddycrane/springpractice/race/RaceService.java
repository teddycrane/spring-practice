package com.teddycrane.springpractice.race;

import com.teddycrane.springpractice.race.model.IRaceService;
import com.teddycrane.springpractice.race.model.RaceResult;
import com.teddycrane.springpractice.racer.Racer;
import com.teddycrane.springpractice.enums.Category;
import com.teddycrane.springpractice.enums.RaceFilterType;
import com.teddycrane.springpractice.error.*;
import com.teddycrane.springpractice.helper.EnumHelpers;
import com.teddycrane.springpractice.models.Either;
import com.teddycrane.springpractice.race.model.RaceRepository;
import com.teddycrane.springpractice.racer.model.RacerRepository;
import com.teddycrane.springpractice.models.BaseService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RaceService extends BaseService implements IRaceService
{

	private final RaceRepository raceRepository;

	private final RacerRepository racerRepository;

	public RaceService(RaceRepository raceRepository, RacerRepository racerRepository)
	{
		super();
		this.raceRepository = raceRepository;
		this.racerRepository = racerRepository;
	}

	@Override
	public List<Race> getAllRaces()
	{
		this.logger.trace("getAllRaces called");
		Iterable<Race> response = raceRepository.findAll();
		List<Race> result = new ArrayList<>();
		response.forEach(result::add);
		return result;
	}

	@Override
	public Race getRace(UUID id) throws RaceNotFoundException
	{
		this.logger.trace("getRace called");
		Optional<Race> result = this.raceRepository.findById(id);

		if (result.isPresent())
		{
			return new Race(result.get());
		} else
		{
			this.logger.error("Unable to find a race with the id {}", id);
			throw new RaceNotFoundException(String.format("Unable to find a race with id %s\n", id));
		}
	}

	@Override
	public Race createRace(String name, Category category) throws DuplicateItemException
	{
		this.logger.trace("createRace called");
		Optional<Race> existing = this.raceRepository.findByName(name);

		if (existing.isPresent())
		{
			if (existing.get().getCategory() == category && existing.get().getName().equals(name))
			{
				this.logger.error("Name collision detected! A race with the name {} and the same category already exists!", name);
				throw new DuplicateItemException(
						String.format("An event for category %s with name %s already exists!", EnumHelpers.getCategoryMapping(category), name));
			}
		}
		return this.raceRepository.save(new Race(name, category));
	}

	@Override
	public Race createRace(String name, Category category, Date startTime) throws DuplicateItemException
	{
		this.logger.trace("createRace called");

		Optional<Race> existing = this.raceRepository.findByName(name);

		if (existing.isPresent())
		{
			Race race = new Race(existing.get());
			if (race.getName().equals(name) && race.getCategory() == category && race.getStartTime().equals(startTime))
			{
				this.logger.error("Name collision detected! A race with the name {} in the same category already exists!", name);
				throw new DuplicateItemException("A race with this same name already exists!");
			}
		}
		return this.raceRepository.save(new Race(name, category, startTime));
	}

	@Override
	public Race createRace(String name, Category category, Date startTime, Date endTime) throws DuplicateItemException
	{
		this.logger.trace("createRace called");

		Optional<Race> existing = this.raceRepository.findByName(name);

		if (existing.isPresent())
		{
			Race race = new Race(existing.get());

			if (race.getName().equals(name) && race.getCategory() == category)
			{
				this.logger.error("Name collision detected! A race with the name {} in the same category already exists!", name);
				throw new DuplicateItemException("A race with the same name already exists!");
			}
		}
		return this.raceRepository.save(new Race(name, category, startTime, endTime));
	}

	@Override
	public Race updateRace(UUID id, String name, Category category) throws UpdateException, RaceNotFoundException, DuplicateItemException
	{
		this.logger.trace("updateRace called");
		Optional<Race> _race = this.raceRepository.findById(id);

		if (_race.isPresent())
		{
			Race race = new Race(_race.get());

			if (name != null)
			{
				race.setName(name);
			}
			if (category != null)
			{
				race.setCategory(category);
			}

			Optional<Race> other = this.raceRepository.findByName(name);

			// name collision validation
			if (other.isPresent())
			{
				if (other.get().getName().equals(name) && other.get().getCategory() == category)
				{
					this.logger.error("An item with the name {} in the same category already exists.  Please try again.", name);
					throw new DuplicateItemException(String.format("An item with the name %s and category %s already exists!", name, EnumHelpers.getCategoryMapping(category)));
				}
			}

			return this.raceRepository.save(race);
		} else
		{
			this.logger.error("Unable to find a race with the id {}", id);
			throw new RaceNotFoundException(String.format("Unable to find a race with id %s", id));
		}
	}

	@Override
	public Race addRacer(UUID id, List<UUID> racerIds) throws RacerNotFoundException, RaceNotFoundException, UpdateException
	{
		this.logger.trace("addRacer called");

		// remove duplicates from within the list of ids
		List<Racer> racers;
		Optional<Race> _race = this.raceRepository.findById(id);
		Race r;

		if (_race.isPresent())
		{
			r = new Race(_race.get());

			// throw exception if the race has already started
			if (r.isStarted())
			{
				logger.error("Cannot add racers to a race that is already started! Start time: {}", r.getStartTime());
				throw new UpdateException("Cannot add racers to a race that has already started!");
			}

			// create set to de-dupe
			racers = new ArrayList<>(r.getRacers());
			Set<UUID> idSet = racers.stream().map(Racer::getId).collect(Collectors.toSet());
			List<UUID> deDupedIds = racerIds.stream().filter((element) -> !idSet.contains(element)).collect(Collectors.toList());

			try
			{
				// since we've removed duplicates earlier, we just forEach the iterable onto the list of racers in the Race object
				Iterable<Racer> _racers = this.racerRepository.findAllById(deDupedIds);
				_racers.forEach(r::addRacer);
			} catch (IllegalArgumentException e)
			{
				this.logger.error("Unable to find a racer! More info: ");
				this.logger.error(e.getMessage());
				throw new RacerNotFoundException("Cannot find a racer with a null id or entry.  ");
			}

			return this.raceRepository.save(r);
		} else
		{
			this.logger.error("Unable to find a race with the id {}", id);
			throw new RaceNotFoundException(String.format("Unable to find Race with id %s", id));
		}

	}

	@Override
	public Race startRace(UUID id) throws RaceNotFoundException, StartException
	{
		this.logger.trace("RaceService.startRace called!");
		Optional<Race> _race = this.raceRepository.findById(id);

		if (_race.isPresent())
		{
			Race race = new Race(_race.get());

			// check if the start time exists, and if so, throw error
			if (race.getStartTime() != null)
			{
				this.logger.error("Unable to start a race that has already been started!");
				throw new StartException("Unable to start a race that has already been started!");
			} else if (race.getEndTime() != null)
			{
				this.logger.error("Unable to start a race that has already finished!");
				throw new StartException("Unable to start a race that is already finished");
			} else
			{
				this.logger.info(String.format("Starting race %s at time %s", id, new Date()));
				race.setStartTime(new Date());
				return this.raceRepository.save(race);
			}
		} else
		{
			String message = String.format("Unable to find a race with the id %s", id);
			this.logger.error(message);
			throw new RaceNotFoundException(message);
		}
	}

	@Override
	public Race endRace(UUID id) throws RaceNotFoundException, EndException
	{
		this.logger.trace("endRace called");
		Optional<Race> _race = this.raceRepository.findById(id);

		if (_race.isPresent())
		{
			Race race = new Race(_race.get());

			// if the start time exists and is in the past, continue
			if (race.getStartTime() == null)
			{
				this.logger.error("Unable to end a race that has not started");
				throw new EndException("Unable to end a race that has not started!");
			} else if (race.getStartTime().after(new Date()))
			{
				this.logger.error("Unable to end a race that starts in the future!");
				throw new EndException("Unable to start a race that starts in the future");
			} else if (race.getEndTime() != null)
			{
				this.logger.error("Unable to end a race that has already finished!");
				throw new EndException("Unable to end a race that has already finished!");
			} else
			{
				// if the checks above ALL fail, then the race is able to be finished
				race.setEndTime(new Date());
				return this.raceRepository.save(race);
			}
		} else
		{
			String message = String.format("Unable to find a race with the id %s", id);
			this.logger.error(message);
			throw new RaceNotFoundException(message);
		}
	}

	@Override
	public Race placeRacersInFinishOrder(UUID raceId, List<UUID> requestIds) throws RaceNotFoundException, RacerNotFoundException, DuplicateItemException, StartException
	{
		this.logger.trace("placeRacerInFinishOrder called");

		Optional<Race> _race = this.raceRepository.findById(raceId);

		if (_race.isPresent())
		{
			this.logger.trace("Race for id {} found!", raceId);
			Race race = new Race(_race.get());
			Iterable<Racer> result = this.racerRepository.findAllById(requestIds);
			ArrayList<Racer> racers = new ArrayList<>();
			result.forEach(racers::add);

			if (racers.size() < requestIds.size())
			{
				this.logger.error("One or more of the provided ids was not found, {}", racers);
				throw new RacerNotFoundException(String.format("one or more of the provided id's was not found.  Ids: %s", racers));
			} else if (race.getStartTime() != null)
			{
				this.logger.trace("Placing racers {}", racers);
				// ensure that the race has been started
				// set finish date for racers to now
				Date finishDate = new Date();
				Map<Racer, Date> resultMap = race.getFinishOrder();
				racers.forEach((racer) -> resultMap.put(racer, finishDate));
				race.setFinishOrder(resultMap);
				return this.raceRepository.save(race);
			} else
			{
				this.logger.error("Cannot set finishers for a race that has not been started!");
				throw new StartException("Cannot set finishers for a race that is not started");
			}
		} else
		{
			String message = String.format("Unable to find a race with the id %s", raceId);
			this.logger.error("Unable to find a race with the id {}", raceId);
			throw new RaceNotFoundException(message);
		}
	}

	@Override
	public RaceResult getResults(UUID raceId) throws RaceNotFoundException
	{
		this.logger.trace("getResults called");

		Optional<Race> _race = this.raceRepository.findById(raceId);

		if (_race.isPresent())
		{
			this.logger.trace("Race for id {} found!", raceId);

			RaceResult result;
			Race race = _race.get();
			Map<Racer, Date> finishTimes = new HashMap<>(race.getFinishOrder());

			// put entries into stream for sorting
			List<Racer> sorted = finishTimes
					.entrySet()
					.parallelStream()
					.sorted(Map.Entry.comparingByValue())
					.map(Map.Entry::getKey)
					.collect(Collectors.toList());

			result = new RaceResult(race.getName(), race.getCategory(), sorted);

			return result;
		} else
		{
			this.logger.error("Unable to find a race with the id {}", raceId);
			throw new RaceNotFoundException(String.format("Unable to find a race with the provided id %s", raceId));
		}
	}

	@Override
	public Race deleteRace(UUID raceId) throws RaceNotFoundException
	{
		this.logger.trace("deleteRace called");
		Optional<Race> _race = this.raceRepository.findById(raceId);

		if (_race.isPresent())
		{
			Race race = new Race(_race.get());

			this.logger.trace("Found a race with the id {}", raceId);
			this.raceRepository.delete(race);

			return race;
		} else
		{
			this.logger.error("Unable to find a race with the id {}", raceId);
			throw new RaceNotFoundException(String.format("Unable to find a race with the id %s", raceId));
		}
	}


	@Override
	public Map<UUID, Integer> getResultsForRacer(UUID id) throws RacerNotFoundException
	{
		logger.trace("getResultsForRacer called");

		Optional<Racer> _racer = this.racerRepository.findById(id);

		if (_racer.isPresent())
		{
			Map<UUID, Integer> response = new HashMap<>();
			List<Race> races = new ArrayList<>();
			this.raceRepository.findAllById(this.raceRepository.findRacesWithRacer(id.toString())).forEach(races::add);

			races.forEach((race) -> {
				logger.info("Calculating finish place for racer {} in race {}", id, race.getId());
				response.put(race.getId(), race.getFinishPlace(id));
			});

			return response;
		} else
		{
			logger.error("Unable to find a racer with id {}", id);
			throw new RacerNotFoundException("Unable to find a racer with the provided id!");
		}
	}

	@Override
	public Collection<Race> filterRace(RaceFilterType filter, Either<String, Category> value) throws BadRequestException, InternalServerError
	{
		logger.trace("filterRace called");
		Optional<String> name = value.fromLeft();
		Optional<Category> category = value.fromRight();

		switch (filter)
		{
			case CATEGORY:
			{
				if (category.isEmpty())
				{
					logger.error("No category value supplied! Value provided: {}", value);
					throw new InternalServerError("Internal server error");
				}

				return this.raceRepository.findByCategory(category.get());
			}
			case NAME:
			{
				if (name.isEmpty())
				{
					logger.error("No name provided! Value provided: {}", value);
					throw new InternalServerError("Internal Server Error");
				}

				return this.raceRepository.findByNameContaining(name.get());
			}
			default:
			{
				logger.error("No filter type provided");
				throw new BadRequestException("No filter type provided!");
			}
		}
	}
}
