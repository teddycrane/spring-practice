package com.teddycrane.springpractice.tests.helpers;

import com.teddycrane.springpractice.entity.Event;
import com.teddycrane.springpractice.entity.Race;
import com.teddycrane.springpractice.entity.Racer;
import com.teddycrane.springpractice.enums.Category;
import org.apache.commons.lang3.RandomStringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TestResourceGenerator
{

	public static @NotNull Racer generateRacer()
	{
		String firstName = RandomStringUtils.randomAlphabetic(10);
		String lastName = RandomStringUtils.randomAlphabetic(10);
		return new Racer(firstName, lastName);
	}

	public static @NotNull List<Racer> generateRacerList(int length)
	{
		List<Racer> result = new ArrayList<>(length);
		for (int i = 0; i < length; i++)
			result.add(i, generateRacer());
		return result;
	}

	public static @NotNull Race generateRace()
	{
		String name = RandomStringUtils.randomAlphabetic(10);
		ArrayList<Racer> temp = new ArrayList<>();
		for (int i = 0; i < 5; i++)
			temp.add(generateRacer());

		Race result = new Race(name);
		result.setRacers(temp);
		return result;
	}

	public static @NotNull List<Race> generateRaceList(int length)
	{
		List<Race> result = new ArrayList<>(length);
		for (int i = 0; i < length; i++)
		{
			Race temp = generateRace();
			temp.setRacers(generateRacerList(5));
			result.add(i, temp);
		}
		return result;
	}

	public static @NotNull Event generateEvent()
	{
		Event e = new Event();
		e.setName(RandomStringUtils.randomAlphabetic(10));
		return e;
	}

	public static @NotNull List<Event> generateEventList(int size)
	{
		List<Event> result = new ArrayList<>(size);
		for (int i = 0; i < size; i++)
		{
			result.add(generateEvent());
		}

		return result;
	}
}
