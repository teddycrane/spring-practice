package com.teddycrane.springpractice.service;

import com.teddycrane.springpractice.entity.Racer;
import com.teddycrane.springpractice.enums.Category;
import com.teddycrane.springpractice.exceptions.RacerNotFoundException;
import com.teddycrane.springpractice.repository.RacerRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RacerService implements IRacerService
{

	private final Logger logger;
	private final RacerRepository racerRepository;

	public RacerService(RacerRepository racerRepository)
	{
		this.logger = LogManager.getLogger(this.getClass());
		this.racerRepository = racerRepository;
	}

	@Override
	public List<Racer> getAllRacers()
	{
		this.logger.trace("getAllRacers called");
		List<Racer> allRacers = new ArrayList<>();
		racerRepository.findAll().forEach(allRacers::add);

		allRacers.removeIf(Racer::getIsDeleted);
		return allRacers;
	}

	@Override
	public Racer getRacerById(UUID id) throws RacerNotFoundException
	{
		this.logger.trace("getRacerById called");
		Optional<Racer> result = racerRepository.findById(id);

		if (result.isPresent())
		{
			return result.get();
		} else
		{
			this.logger.error("No racer found with the id {}", id);
			throw new RacerNotFoundException("No racer found!");
		}
	}

	@Override
	public Racer addRacer(String firstName, String lastName)
	{
		this.logger.trace("addRacer called");
		Racer r = new Racer(firstName, lastName);
		return racerRepository.save(r);
	}

	@Override
	public Racer updateRacer(UUID id, String firstName, String lastName, Category category) throws RacerNotFoundException
	{
		this.logger.trace("updateRacer called");
		Optional<Racer> r = this.racerRepository.findById(id);
		Racer racer;

		if (r.isPresent())
		{
			racer = new Racer(r.get());
			if (racer.getIsDeleted())
			{
				this.logger.error("The racer for id {} is deleted, and is unable to be edited", id);
				throw new RacerNotFoundException("No valid racer found with the provided id");
			}
			if (firstName != null) racer.setFirstName(firstName);
			if (lastName != null) racer.setLastName(lastName);
			if (category != null) racer.setCategory(category);

			return this.racerRepository.save(racer);
		} else
		{
			this.logger.error("Unable to find a racer with id {}", id);
			throw new RacerNotFoundException("Unable to find the specified racer.");
		}
	}

	@Override
	public Racer deleteRacer(UUID id) throws RacerNotFoundException
	{
		this.logger.trace("deleteRacer called!");
		Optional<Racer> r = this.racerRepository.findById(id);
		Racer racer;

		if (r.isPresent())
		{
			racer = new Racer(r.get());
			if (racer.getIsDeleted())
			{
				this.logger.info("Deleting racer:\n{}.", racer);
				this.racerRepository.delete(racer);
				return racer;
			}

			racer.setIsDeleted(true);
			this.racerRepository.save(racer);
			return racer;
		} else
		{
			this.logger.error("Unable to find a racer with id {}", id);
			throw new RacerNotFoundException("No racer found!");
		}
	}

	@Override
	public List<Racer> getAllRacersWithDeleted()
	{
		this.logger.trace("getAllRacersWithDeleted called!");
		List<Racer> result = new ArrayList<>();
		Iterable<Racer> response = this.racerRepository.findAll();
		response.forEach(result::add);
		return result;
	}

	@Override
	public Racer restoreRacer(UUID id) throws RacerNotFoundException
	{
		this.logger.trace("restoreRacer called");

		Optional<Racer> result = this.racerRepository.findById(id);
		Racer r;

		if (result.isPresent())
		{
			r = new Racer(result.get());
			r.setIsDeleted(false);
			return this.racerRepository.save(r);
		} else
		{
			this.logger.trace("Unable to find with id {}", id);
			throw new RacerNotFoundException(String.format("Unable to find a racer with id %s", id));
		}
	}

}
