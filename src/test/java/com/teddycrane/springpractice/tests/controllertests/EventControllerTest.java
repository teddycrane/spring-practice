package com.teddycrane.springpractice.tests.controllertests;

import com.teddycrane.springpractice.event.EventController;
import com.teddycrane.springpractice.event.model.IEventController;
import com.teddycrane.springpractice.error.BadRequestException;
import com.teddycrane.springpractice.error.DuplicateItemException;
import com.teddycrane.springpractice.error.EventNotFoundException;
import com.teddycrane.springpractice.event.Event;
import com.teddycrane.springpractice.event.request.CreateEventRequest;
import com.teddycrane.springpractice.event.model.IEventService;
import com.teddycrane.springpractice.tests.helpers.TestResourceGenerator;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

public class EventControllerTest {
	private IEventController eventController;

	private final UUID testId = UUID.randomUUID();
	private final String testString = testId.toString();

	@Mock
	private IEventService eventService;

	private Event event;
	private List<Event> eventList;

	@Captor
	private ArgumentCaptor<UUID> idCaptor;

	@BeforeEach
	public void init() {
		MockitoAnnotations.openMocks(this);
		this.eventController = new EventController(eventService);
		event = TestResourceGenerator.generateEvent();
		eventList = TestResourceGenerator.generateEventList(10);
	}

	@Test
	@DisplayName("Get Event should return data")
	public void getEvent_shouldReturnAnEvent() {
		when(this.eventService.getEvent(testId)).thenReturn(event);

		// test
		Event result = this.eventController.getEvent(testString);
		verify(this.eventService).getEvent(idCaptor.capture());

		Assertions.assertEquals(testId, idCaptor.getValue());
		Assertions.assertEquals(event, result);
	}

	@Test
	@DisplayName("Get Event should handle service errors")
	public void getEvent_shouldHandleServiceErrors() {
		when(this.eventService.getEvent(testId)).thenThrow(EventNotFoundException.class);

		Assertions.assertThrows(EventNotFoundException.class, () -> this.eventController.getEvent(testString));

		Assertions.assertThrows(BadRequestException.class, () -> this.eventController.getEvent("not a uuid"));
	}

	@Test
	public void getAllEvents_shouldReturnAllEvents() {
		when(this.eventService.getAllEvents()).thenReturn(eventList);

		// test
		List<Event> result = this.eventController.getAllEvents();
		Assertions.assertEquals(10, result.size());
		for (int i = 0; i < result.size(); i++) {
			Assertions.assertEquals(eventList.get(i), result.get(i));
		}
	}

	@Test
	public void createEvent_shouldCreateEvent() {
		when(this.eventService.createEvent(any(String.class), any(Date.class), any(Date.class))).thenReturn(event);

		// test
		Event result = this.eventController.createEvent(new CreateEventRequest("name", new Date(), new Date()));
		Assertions.assertTrue(result.equals(event));
	}

	@Test
	public void createEvent_shouldErrorOnEmptyRequest() {
		CreateEventRequest request = new CreateEventRequest();

		assertThrows(BadRequestException.class, () -> this.eventController.createEvent(request));
	}

	@Test
	public void createEvent_shouldDisallowDuplicates() {
		when(this.eventService.createEvent(any(String.class), any(Date.class), any(Date.class)))
				.thenThrow(DuplicateItemException.class);
		CreateEventRequest request = new CreateEventRequest("test", new Date(), new Date());

		assertThrows(DuplicateItemException.class, () -> this.eventController.createEvent(request));
	}

	@Test
	public void deleteEvent_shouldDeleteEvent() {
		when(this.eventService.deleteEvent(any(UUID.class))).thenReturn(event);

		// test
		Event result = this.eventController.deleteEvent(UUID.randomUUID().toString());
		Assertions.assertTrue(result.equals(event));
	}

	@Test
	public void deleteEvent_shouldHandleBadId() {
		assertThrows(BadRequestException.class, () -> this.eventController.deleteEvent("bad id"));
	}

	@Test
	public void deleteEvent_shouldHandleNoEvent() {
		when(this.eventService.deleteEvent(testId)).thenThrow(EventNotFoundException.class);

		assertThrows(EventNotFoundException.class, () -> this.eventController.deleteEvent(testString));
	}
}
