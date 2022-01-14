package com.teddycrane.springpractice.tests.servicetests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.teddycrane.springpractice.error.DuplicateItemException;
import com.teddycrane.springpractice.event.Event;
import com.teddycrane.springpractice.event.EventService;
import com.teddycrane.springpractice.event.model.EventRepository;
import com.teddycrane.springpractice.event.model.IEventService;
import com.teddycrane.springpractice.race.Race;
import com.teddycrane.springpractice.race.model.RaceRepository;
import com.teddycrane.springpractice.tests.helpers.TestResourceGenerator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class EventServiceTest {
  @Mock private EventRepository eventRepository;

  @Mock private RaceRepository raceRepository;

  private IEventService eventService;

  private Event expected;
  private UUID testId;
  private List<Race> raceList;

  @Captor private ArgumentCaptor<Event> eventCaptor;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    this.eventService =
        new EventService(this.eventRepository, this.raceRepository);
    this.expected = TestResourceGenerator.generateEvent();
    this.testId = expected.getId();
    this.raceList = TestResourceGenerator.generateRaceList(5);

    // set up fallback database mocking
    when(this.eventRepository.findById(testId))
        .thenReturn(Optional.of(expected));
  }

  @Test
  public void getEvent_shouldReturnData() {
    Event actual = this.eventService.getEvent(testId);

    assertEquals(expected, actual);
  }

  @Test
  public void createEvent_shouldCreateEvent() {
    Event newEvent = new Event();
    when(this.eventRepository.save(any(Event.class))).thenReturn(newEvent);

    Event actual =
        this.eventService.createEvent("event name", new Date(), new Date());
    assertNotNull(actual);
  }

  @Test
  public void createEvent_shouldCreateEventWithNoEndDate() {
    // when(this.eventRepository.save(any(Event.class))).thenReturn(newEvent);
    Event newEvent = new Event("name");
    newEvent.setStartDate(new Date());

    // Event actual = this.eventService.createEvent()
  }

  @Test
  public void createEvent_shouldHandleNameCollisions() {
    when(this.eventRepository.findByName("name"))
        .thenReturn(Optional.of(expected));

    assertThrows(DuplicateItemException.class,
                 ()
                     -> this.eventService.createEvent("name",
                                                      expected.getStartDate(),
                                                      expected.getEndDate()));
  }
}
