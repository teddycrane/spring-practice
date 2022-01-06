package com.teddycrane.springpractice.tests.unittests;

import com.teddycrane.springpractice.race.Race;
import com.teddycrane.springpractice.racer.Racer;
import com.teddycrane.springpractice.tests.helpers.TestResourceGenerator;
import com.teddycrane.springpractice.enums.Category;

import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.*;

public class RaceTest {

	private Race race;

	@BeforeEach
	public void init() {
		race = new Race();
	}

	@Test
	public void constructorShouldCreateValidRaces() {
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
	public void shouldSetRacers() {
		List<Racer> list = new ArrayList<>();
		list.add(new Racer("fname", "lname"));

		race.setRacers(list);

		List<Racer> racers = race.getRacers();
		boolean areEqual = false;

		// todo update this
		if (racers.size() == list.size()) {
			for (int i = 0; i < racers.size(); i++) {
				areEqual = racers.get(i).equals(list.get(i));
			}
		}

		Assertions.assertTrue(areEqual);
	}

	@Test
	public void shouldTestGetterAndSetter() {
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
	public void shouldTestAllCasesForEqualsAndHashCode() {
		Race race1 = new Race();
		Race race2 = new Race();
		Race race3 = new Race(race1);

		List<Racer> list = new ArrayList<>();
		list.add(new Racer());
		race2.setRacers(list);

		Assertions.assertEquals(race1, race3);
		Assertions.assertEquals(race1.hashCode(), race3.hashCode());
		Assertions.assertFalse(race1.equals(race2));

		Assertions.assertFalse(race1.equals(""));
	}

	@Test
	public void shouldProperlyFormatToString() {
		String raceString = race.toString();

		// basic test because testing a toString function is overkill
		Assertions.assertNotNull(raceString);

		List<Racer> racers = TestResourceGenerator.generateRacerList(5);
		race.setRacers(racers);

		Assertions.assertNotNull(race.toString());
	}

	@Test
	public void getFinishPlaceTest() {
		List<Racer> racers = TestResourceGenerator.generateRacerList(5);
		race.setRacers(racers);
		Map<Racer, Date> finishOrder = new HashMap<>();
		finishOrder.put(racers.get(0), new Date());
		race.setFinishOrder(finishOrder);

		Assertions.assertTrue(race.getFinishPlace(racers.get(0).getId()) > 0);

		finishOrder.put(racers.get(1), new Date());
		race.setFinishOrder(finishOrder);
		Assertions.assertTrue(race.getFinishPlace(racers.get(1).getId()) > 0);
	}

	@Test
	public void testIsStarted() {
		try {
			Assertions.assertFalse(race.isStarted());
			race.setStartTime(new Date());
			Thread.sleep(100);

			Assertions.assertTrue(race.isStarted());

			race.setStartTime(null);
			Assertions.assertFalse(race.isStarted());
		} catch (Exception e) {
			Assertions.assertTrue(false);
		}
	}
}
