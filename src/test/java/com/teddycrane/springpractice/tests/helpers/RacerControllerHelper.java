package com.teddycrane.springpractice.tests.helpers;

import com.teddycrane.springpractice.entity.Racer;
import org.apache.commons.lang3.RandomStringUtils;

public class RacerControllerHelper
{

	public static Racer generateRacer()
	{
		String firstName = RandomStringUtils.randomAlphabetic(10);
		String lastName = RandomStringUtils.randomAlphabetic(10);
		return new Racer(firstName, lastName);
	}
}
