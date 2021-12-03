package com.teddycrane.springpractice.tests.controllertests;

import com.teddycrane.springpractice.controller.EventController;
import com.teddycrane.springpractice.controller.model.IEventController;
import com.teddycrane.springpractice.entity.Event;
import com.teddycrane.springpractice.models.CreateEventRequest;
import com.teddycrane.springpractice.service.model.IEventService;
import com.teddycrane.springpractice.tests.helpers.TestResourceGenerator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EventControllerTest
{
	private IEventController eventController;

	@Mock
	private IEventService eventService;

	private Event event;
	private List<Event> eventList;

	@Before
	public void init()
	{
		MockitoAnnotations.openMocks(this);
		this.eventController = new EventController(eventService);
		event = TestResourceGenerator.generateEvent();
		eventList = TestResourceGenerator.generateEventList(10);
	}

	@Test
	public void shouldReturnAnEvent()
	{
		when(this.eventService.getEvent(any(UUID.class))).thenReturn(event);

		// test
		Event result = this.eventController.getEvent(UUID.randomUUID().toString());
		Assert.assertTrue(event.equals(result));
	}

	@Test
	public void shouldReturnAllEvents()
	{
		when(this.eventService.getAllEvents()).thenReturn(eventList);

		// test
		List<Event> result = this.eventController.getAllEvents();
		Assert.assertEquals(10, result.size());
		for (int i = 0; i < result.size(); i++)
		{
			Assert.assertTrue(result.get(i).equals(eventList.get(i)));
		}
	}

	@Test
	public void shouldCreateEvent()
	{
		when(this.eventService.createEvent(any(String.class), any(Date.class), any(Date.class))).thenReturn(event);

		// test
		Event result = this.eventController.createEvent(new CreateEventRequest("name", new Date(), new Date()));
		Assert.assertTrue(result.equals(event));
	}

	@Test
	public void shouldDeleteEvent()
	{
		when(this.eventService.deleteEvent(any(UUID.class))).thenReturn(event);

		// test
		Event result = this.eventController.deleteEvent(UUID.randomUUID().toString());
		Assert.assertTrue(result.equals(event));
	}
}

