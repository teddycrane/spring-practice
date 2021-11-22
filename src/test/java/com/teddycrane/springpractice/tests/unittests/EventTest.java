package com.teddycrane.springpractice.tests.unittests;

import com.teddycrane.springpractice.entity.Event;
import com.teddycrane.springpractice.entity.Race;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class EventTest
{

	private Event event;

	@Before
	public void init()
	{
		event = new Event();
		event.setName("Test Event");
		event.setStartDate(new Date());
		event.setEndDate(new Date());
		List<Race> tempList = new ArrayList<>();
		tempList.add(new Race());
		event.setRaces(tempList);
	}

	@Test
	public void constructorShouldCreate()
	{
		Assert.assertNotNull(event);

		Event testEvent = new Event("Test Name 1");
		Assert.assertEquals("Test Name 1", testEvent.getName());

		testEvent = new Event("Test Name 2", new Date(1), new Date(2));
		Assert.assertEquals("Test Name 2", testEvent.getName());
		Assert.assertEquals(new Date(1), testEvent.getStartDate());
		Assert.assertEquals(new Date(2), testEvent.getEndDate());

		testEvent = new Event(event);
		Assert.assertEquals(event.getName(), testEvent.getName());
		Assert.assertEquals(event.getStartDate(), testEvent.getStartDate());
		Assert.assertEquals(event.getEndDate(), testEvent.getEndDate());
		Assert.assertEquals(event.getRaces(), testEvent.getRaces());
		Assert.assertEquals(event.getId(), testEvent.getId());

		testEvent = new Event("Test Name 3", new Date(3));
		Assert.assertEquals("Test Name 3", testEvent.getName());
		Assert.assertEquals(new Date(3), testEvent.getStartDate());
	}

	@Test
	public void twoEventsShouldBeEqual()
	{
		Event test = new Event();
		Event test2 = new Event(test);
		Assert.assertTrue(test.equals(test2));
	}
}
