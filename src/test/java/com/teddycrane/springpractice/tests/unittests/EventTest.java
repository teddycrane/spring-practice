package com.teddycrane.springpractice.tests.unittests;

import com.teddycrane.springpractice.event.Event;
import com.teddycrane.springpractice.race.Race;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.*;

public class EventTest {

  private Event event;

  @BeforeEach
  public void init() {
    event = new Event();
    event.setName("Test Event");
    event.setStartDate(new Date());
    event.setEndDate(new Date());
    List<Race> tempList = new ArrayList<>();
    tempList.add(new Race());
    event.setRaces(tempList);
  }

  @Test
  public void constructorShouldCreate() {
    Assertions.assertNotNull(event);

    Event testEvent = new Event("Test Name 1");
    Assertions.assertEquals("Test Name 1", testEvent.getName());

    testEvent = new Event("Test Name 2", new Date(1), new Date(2));
    Assertions.assertEquals("Test Name 2", testEvent.getName());
    Assertions.assertEquals(new Date(1), testEvent.getStartDate());
    Assertions.assertEquals(new Date(2), testEvent.getEndDate());

    testEvent = new Event(event);
    Assertions.assertEquals(event.getName(), testEvent.getName());
    Assertions.assertEquals(event.getStartDate(), testEvent.getStartDate());
    Assertions.assertEquals(event.getEndDate(), testEvent.getEndDate());
    Assertions.assertEquals(event.getRaces(), testEvent.getRaces());
    Assertions.assertEquals(event.getId(), testEvent.getId());

    testEvent = new Event("Test Name 3", new Date(3));
    Assertions.assertEquals("Test Name 3", testEvent.getName());
    Assertions.assertEquals(new Date(3), testEvent.getStartDate());
  }

  @Test
  public void twoEventsShouldBeEqual() {
    Event test = new Event();
    Event test2 = new Event(test);
    Assertions.assertTrue(test.equals(test2));
  }
}
