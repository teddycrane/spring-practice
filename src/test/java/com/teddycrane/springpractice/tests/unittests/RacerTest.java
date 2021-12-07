package com.teddycrane.springpractice.tests.unittests;

import com.teddycrane.springpractice.racer.Racer;
import com.teddycrane.springpractice.enums.Category;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.junit.Assert;


@RunWith(MockitoJUnitRunner.class)
public class RacerTest
{

	private Racer racer;

	@Before
	public void init()
	{
		racer = new Racer();
		racer.setFirstName("test");
		racer.setLastName("user");
	}

	@Test
	public void constructorShouldCreateValidRacers()
	{
		// test that the default constructor constructs properly and that the constructors are all working
		Assert.assertNotNull(racer);
		Assert.assertTrue(racer.equals(new Racer(racer)));
		Assert.assertEquals(racer.getId(), new Racer(racer).getId());

		// test that the full constructor creates valid racers
		Racer fullConstructorTestRacer = new Racer("fname", "lname");

		Assert.assertNotNull(fullConstructorTestRacer);
		Assert.assertEquals(fullConstructorTestRacer.getFirstName(), "fname");
		Assert.assertEquals(fullConstructorTestRacer.getLastName(), "lname");
		Assert.assertFalse(fullConstructorTestRacer.getIsDeleted());
		Assert.assertEquals(fullConstructorTestRacer.getCategory(), Category.CAT5);
	}

	@Test
	public void testGetterAndSetter()
	{
		// test first name setter
		racer.setFirstName("set first name");
		Assert.assertEquals("set first name", racer.getFirstName());

		// test last name setter
		racer.setLastName("set last name");
		Assert.assertEquals(racer.getLastName(), "set last name");

		// test category setter
		racer.setCategory(Category.CAT1);
		Assert.assertEquals(racer.getCategory(), Category.CAT1);

		// test deletion setter
		racer.setIsDeleted(true);
		Assert.assertTrue(racer.getIsDeleted());
	}

	@Test
	public void testToStringOverride()
	{
		String[] test = racer.toString().split("\n");

		Assert.assertEquals(test[0], "{");
		Assert.assertEquals(test[1], String.format("    \"id\": \"%s\",", racer.getId()));
		Assert.assertEquals(test[2], "    \"firstName\": \"test\",");
		Assert.assertEquals(test[3], "    \"lastName\": \"user\",");
		Assert.assertEquals(test[4], "    \"category\": \"Category 5\",");
		Assert.assertEquals(test[5], "    \"isDeleted\": \"false\"");
		Assert.assertEquals(test[6], "}");
	}

	@Test
	public void testHashCode()
	{
		int hash1 = racer.hashCode();
		int hash2 = new Racer(racer).hashCode();
		int hash3 = new Racer().hashCode();

		Assert.assertEquals(hash1, hash2);
		Assert.assertNotEquals(hash1, hash3);
	}
}
