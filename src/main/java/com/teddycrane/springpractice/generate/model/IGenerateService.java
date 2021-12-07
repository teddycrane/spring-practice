package com.teddycrane.springpractice.generate.model;

import com.teddycrane.springpractice.race.Race;
import com.teddycrane.springpractice.racer.Racer;
import com.teddycrane.springpractice.enums.Category;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public interface IGenerateService
{

	Collection<Racer> generateRacers(Integer number, Category category);

	Collection<Racer> generateRacers(Integer number);

	Collection<Racer> generateRacer(Category category);

	Collection<Racer> generateRacer();

	Collection<Race> generateRace(Integer number);
}
