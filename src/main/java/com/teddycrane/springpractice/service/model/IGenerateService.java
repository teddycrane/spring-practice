package com.teddycrane.springpractice.service.model;

import com.teddycrane.springpractice.entity.Race;
import com.teddycrane.springpractice.entity.Racer;
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
