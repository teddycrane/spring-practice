package com.teddycrane.springpractice.generate;

import com.github.javafaker.Faker;
import com.teddycrane.springpractice.generate.model.IGenerateService;
import com.teddycrane.springpractice.models.BaseService;
import com.teddycrane.springpractice.race.Race;
import com.teddycrane.springpractice.racer.Racer;
import com.teddycrane.springpractice.enums.Category;
import com.teddycrane.springpractice.race.model.RaceRepository;
import com.teddycrane.springpractice.racer.model.RacerRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class GenerateService extends BaseService implements IGenerateService
{

	private final RacerRepository racerRepository;
	private final RaceRepository raceRepository;
	private final Faker faker;

	public GenerateService(RacerRepository racerRepository, RaceRepository raceRepository)
	{
		super();
		this.racerRepository = racerRepository;
		this.raceRepository = raceRepository;
		this.faker = new Faker();
	}

	@Override
	public Collection<Racer> generateRacers(Integer number, Category category)
	{
		logger.trace("generateRacers called: number: {}, category: {}", number, category);

		Collection<Racer> result = new ArrayList<>();
		for (int i = 0; i < number; i++)
		{
			Racer r = new Racer(faker.name().firstName(), faker.name().lastName(), faker.date().birthday(10, 99));
			r.setCategory(category);
			result.add(this.racerRepository.save(r));
		}

		return result;
	}

	@Override
	public Collection<Racer> generateRacers(Integer number)
	{
		logger.trace("generateRacers called: number: {}", number);

		Collection<Racer> result = new ArrayList<>();

		for (int i = 0; i < number; i++)
		{
			result.add(this.racerRepository.save(new Racer(faker.name().firstName(), faker.name().lastName(), faker.date().birthday(10, 99))));
		}

		return result;
	}

	@Override
	public Collection<Racer> generateRacer(Category category)
	{
		logger.trace("generateRacer called with category: {}", category);

		Collection<Racer> result = new ArrayList<>();
		Racer r = new Racer(faker.name().firstName(), faker.name().lastName(), faker.date().birthday(10, 99));
		r.setCategory(category);
		result.add(this.racerRepository.save(r));

		return result;
	}

	@Override
	public Collection<Racer> generateRacer()
	{
		logger.trace("generateRacer called");

		Collection<Racer> result = new ArrayList<>();
		result.add(this.racerRepository.save(new Racer(faker.name().firstName(), faker.name().lastName(), faker.date().birthday(10, 99))));
		return result;
	}

	@Override
	public Collection<Race> generateRace(Integer number)
	{
		logger.trace("generateRace called");
		Collection<Race> result = new ArrayList<>();

		for (int i = 0; i < number; i++)
			result.add(this.raceRepository.save(new Race(faker.aquaTeenHungerForce().character())));

		return result;
	}
}
