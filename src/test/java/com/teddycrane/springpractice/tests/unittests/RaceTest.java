package com.teddycrane.springpractice.tests.unittests;

import com.teddycrane.springpractice.race.Race;
import com.teddycrane.springpractice.racer.Racer;
import com.teddycrane.springpractice.enums.Category;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.*;

public class RaceTest
{

	private Race race;

	@BeforeEach
	public void init()
	{
		race = new Race();
	}

	@Test
	public void constructorShouldCreateValidRaces()
	{
		// test that a valid race comes from the default constructor
		Assertions.assertNotNull(race);

		// test copy constructor
		Assertions.assertTrue(race.equals(new Race(race)));

		// Test name constructor
		Race namedRace = new Race("name", Category.CAT4);
		Assertions.assertEquals("name", namedRace.getName());
		Assertions.assertEquals(Category.CAT4, namedRace.getCategory());
		Assertions.assertNotNull(namedRace.getId());
		Assertions.assertNotNull(namedRace.getRacers());
	}

	@Test
	public void shouldSetRacers()
	{
		List<Racer> list = new ArrayList<>();
		list.add(new Racer("fname", "lname"));

		race.setRacers(list);

		List<Racer> racers = race.getRacers();
		boolean areEqual = false;

		// todo update this
		if (racers.size() == list.size())
		{
			for (int i = 0; i < racers.size(); i++)
			{
				areEqual = racers.get(i).equals(list.get(i));
			}
		}

		Assertions.assertTrue(areEqual);
	}

	@Test
	public void shouldTestGetterAndSetter()
	{
		// name setter
		race.setName("test");
		Assertions.assertEquals("test", race.getName());

		// category setter
		race.setCategory(Category.CAT1);
		Assertions.assertEquals(Category.CAT1, race.getCategory());

		// adding racer
		race.addRacer(new Racer());
		Assertions.assertEquals(1, race.getRacers().size());
	}

	@Test
	public void shouldTestAllCasesForEquals()
	{
		Race race1 = new Race();
		Race race2 = new Race();
		List<Racer> list = new ArrayList<>();
		list.add(new Racer());
		race2.setRacers(list);

		Assertions.assertFalse(race1.equals(race2));
	}

}
