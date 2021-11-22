package com.teddycrane.springpractice.tests.unittests;

import com.teddycrane.springpractice.entity.Race;
import com.teddycrane.springpractice.entity.Racer;
import com.teddycrane.springpractice.enums.Category;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class RaceTest
{

	private Race race;

	@Before
	public void init()
	{
		race = new Race();
	}

	@Test
	public void constructorShouldCreateValidRaces()
	{
		// test that a valid race comes from the default constructor
		Assert.assertNotNull(race);

		// test copy constructor
		Assert.assertTrue(race.equals(new Race(race)));

		// Test name constructor
		Race namedRace = new Race("name", Category.CAT_4);
		Assert.assertEquals("name", namedRace.getName());
		Assert.assertEquals(Category.CAT_4, namedRace.getCategory());
		Assert.assertNotNull(namedRace.getId());
		Assert.assertNotNull(namedRace.getRacers());
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

		Assert.assertTrue(areEqual);
	}

	@Test
	public void shouldTestGetterAndSetter()
	{
		// name setter
		race.setName("test");
		Assert.assertEquals("test", race.getName());

		// category setter
		race.setCategory(Category.CAT_1);
		Assert.assertEquals(Category.CAT_1, race.getCategory());

		// adding racer
		race.addRacer(new Racer());
		Assert.assertEquals(1, race.getRacers().size());
	}

	@Test
	public void shouldTestAllCasesForEquals()
	{
		Race race1 = new Race();
		Race race2 = new Race();
		List<Racer> list = new ArrayList<>();
		list.add(new Racer());
		race2.setRacers(list);

		Assert.assertFalse(race1.equals(race2));
	}

}
