package com.teddycrane.springpractice.tests.unittests;

import com.teddycrane.springpractice.racer.Racer;

import java.util.Date;

import com.teddycrane.springpractice.enums.Category;

import org.junit.jupiter.api.*;

public class RacerTest {

	private Racer racer;

	@BeforeEach
	public void init() {
		racer = new Racer();
		racer.setFirstName("test");
		racer.setLastName("user");
	}

	@Test
	public void constructorShouldCreateValidRacers() {
		// test that the default constructor constructs properly and that the
		// constructors are all working
		Assertions.assertNotNull(racer);
		Assertions.assertTrue(racer.equals(new Racer(racer)));
		Assertions.assertEquals(racer.getId(), new Racer(racer).getId());

		// test that the full constructor creates valid racers
		Racer fullConstructorTestRacer = new Racer("fname", "lname");

		Assertions.assertNotNull(fullConstructorTestRacer);
		Assertions.assertEquals(fullConstructorTestRacer.getFirstName(), "fname");
		Assertions.assertEquals(fullConstructorTestRacer.getLastName(), "lname");
		Assertions.assertFalse(fullConstructorTestRacer.getIsDeleted());
		Assertions.assertEquals(fullConstructorTestRacer.getCategory(), Category.CAT5);
	}

	@Test
	public void testGetterAndSetter() {
		// test first name setter
		racer.setFirstName("set first name");
		Assertions.assertEquals("set first name", racer.getFirstName());

		// test last name setter
		racer.setLastName("set last name");
		Assertions.assertEquals(racer.getLastName(), "set last name");

		// test category setter
		racer.setCategory(Category.CAT1);
		Assertions.assertEquals(racer.getCategory(), Category.CAT1);

		// test deletion setter
		racer.setIsDeleted(true);
		Assertions.assertTrue(racer.getIsDeleted());
	}

	@Test
	public void testToStringOverride() {
		String result = racer.toString();
		Assertions.assertNotNull(result);
	}

	@Test
	public void testHashCode() {
		int hash1 = racer.hashCode();
		int hash2 = new Racer(racer).hashCode();
		int hash3 = new Racer().hashCode();

		Assertions.assertEquals(hash1, hash2);
		Assertions.assertNotEquals(hash1, hash3);

		racer.setBirthDate(new Date());
		Assertions.assertNotNull(racer.hashCode());
		racer.setIsDeleted(true);
		Assertions.assertNotNull(racer.hashCode());
	}

	@Test
	public void testEquality() {
		Assertions.assertNotEquals(racer, "");
		Racer copy = new Racer(racer);

		Assertions.assertEquals(racer, copy);

		Date date = new Date(System.currentTimeMillis());
		racer = new Racer("firstname", "lastname", date);
		copy = new Racer(racer);

		Assertions.assertEquals(racer, copy);

		copy.setBirthDate(new Date(System.currentTimeMillis() - 10000));
		Assertions.assertNotEquals(racer, copy);

		copy.setIsDeleted(true);
		Assertions.assertNotEquals(racer, copy);

		copy.setLastName("different");
		Assertions.assertNotEquals(racer, copy);

		copy.setFirstName("different");
		Assertions.assertNotEquals(racer, copy);

		copy.setCategory(Category.CAT2);
		Assertions.assertNotEquals(racer, copy);

		Racer other = new Racer();
		Assertions.assertNotEquals(racer, other);
	}
}
