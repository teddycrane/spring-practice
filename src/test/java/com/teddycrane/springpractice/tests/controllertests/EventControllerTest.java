package com.teddycrane.springpractice.tests.controllertests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.teddycrane.springpractice.error.BadRequestException;
import com.teddycrane.springpractice.error.EventNotFoundException;
import com.teddycrane.springpractice.event.Event;
import com.teddycrane.springpractice.event.EventController;
import com.teddycrane.springpractice.event.model.IEventController;
import com.teddycrane.springpractice.event.model.IEventService;
import com.teddycrane.springpractice.event.request.CreateEventRequest;
import com.teddycrane.springpractice.tests.helpers.TestResourceGenerator;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class EventControllerTest {
  private IEventController eventController;

  @Mock private IEventService eventService;

  private Event event;
  private List<Event> eventList;
  private UUID testId;
  private String testString;

  @BeforeEach
  public void init() {
    MockitoAnnotations.openMocks(this);
    this.eventController = new EventController(eventService);
    event = TestResourceGenerator.generateEvent();
    eventList = TestResourceGenerator.generateEventList(10);
    testId = event.getId();
    testString = testId.toString();
  }

  @Test
  public void getEvent_shouldReturnAnEvent() {
    when(this.eventService.getEvent(any(UUID.class))).thenReturn(event);

    // test
    Event result = this.eventController.getEvent(UUID.randomUUID().toString());
    Assertions.assertTrue(event.equals(result));
  }

  @Test
  public void getEvent_shouldHandleServiceErrors() {
    assertThrows(BadRequestException.class,
                 () -> this.eventController.getEvent("bad id"));

    when(this.eventService.getEvent(testId))
        .thenThrow(EventNotFoundException.class);
    assertThrows(EventNotFoundException.class,
                 () -> this.eventController.getEvent(testString));
  }

  @Test
  public void getEvent_shouldReturnAllEvents() {
    when(this.eventService.getAllEvents()).thenReturn(eventList);

    // test
    List<Event> result = this.eventController.getAllEvents();
    Assertions.assertEquals(10, result.size());
    for (int i = 0; i < result.size(); i++) {
      Assertions.assertTrue(result.get(i).equals(eventList.get(i)));
    }
  }

  @Test
  public void createEvent_shouldCreateEvent() {
    when(this.eventService.createEvent(any(String.class), any(Date.class),
                                       any(Date.class)))
        .thenReturn(event);

    // test
    Event result = this.eventController.createEvent(
        new CreateEventRequest("name", new Date(), new Date()));
    Assertions.assertTrue(result.equals(event));
  }

  @Test
  public void deleteEvent_shouldDeleteEvent() {
    when(this.eventService.deleteEvent(any(UUID.class))).thenReturn(event);

    // test
    Event result =
        this.eventController.deleteEvent(UUID.randomUUID().toString());
    Assertions.assertTrue(result.equals(event));
  }
}
