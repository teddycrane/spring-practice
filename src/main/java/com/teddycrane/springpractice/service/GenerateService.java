package com.teddycrane.springpractice.service;

import com.github.javafaker.Faker;
import com.teddycrane.springpractice.entity.Racer;
import com.teddycrane.springpractice.enums.Category;
import com.teddycrane.springpractice.repository.RacerRepository;
import org.springframework.stereotype.Service;

@Service
public class GenerateService extends BaseService implements IGenerateService
{

	private final RacerRepository racerRepository;
	private final Faker faker;

	public GenerateService(RacerRepository racerRepository)
	{
		super();
		this.racerRepository = racerRepository;
		this.faker = new Faker();
	}


	@Override
	public Racer generateSingleRacer()
	{
		logger.trace("generateSingleRacer called");
		Racer r = new Racer(faker.name().firstName(), faker.name().lastName(), faker.date().birthday(10, 99));

		return this.racerRepository.save(r);
	}

	@Override
	public Racer generateSingleRacer(Category category)
	{
		logger.trace("generateSingleRacer called with category {}", category);

		Racer r = new Racer(faker.name().firstName(), faker.name().lastName(), faker.date().birthday(10, 99));
		r.setCategory(category);

		return this.racerRepository.save(r);
	}
}
