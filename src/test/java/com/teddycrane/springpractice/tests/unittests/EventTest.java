package com.teddycrane.springpractice.tests.unittests;

import com.teddycrane.springpractice.event.Event;
import com.teddycrane.springpractice.race.Race;
import com.teddycrane.springpractice.tests.helpers.TestResourceGenerator;
import java.util.ArrayList;
import java.util.Collections;
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
    Assertions.assertEquals(new Date(1), testEvent.getStartDate().orElse(null));
    Assertions.assertEquals(new Date(2), testEvent.getEndDate().orElse(null));

    testEvent = new Event(event);
    Assertions.assertEquals(event.getName(), testEvent.getName());
    Assertions.assertEquals(event.getStartDate(), testEvent.getStartDate());
    Assertions.assertEquals(event.getEndDate(), testEvent.getEndDate());
    Assertions.assertEquals(event.getRaces(), testEvent.getRaces());
    Assertions.assertEquals(event.getId(), testEvent.getId());

    testEvent = new Event("Test Name 3", new Date(3));
    Assertions.assertEquals("Test Name 3", testEvent.getName());
    Assertions.assertEquals(new Date(3), testEvent.getStartDate().orElse(null));
  }

  @Test
  @DisplayName("Equals should be valid in all cases")
  public void equals_shouldTestBranches() {
    Assertions.assertNotEquals(event, "");

    Event other = new Event(event);
    Assertions.assertEquals(event, other);

    // activity state equal
    other.setIsActive(true);
    Assertions.assertNotEquals(event, other);

    // list of races are not equal
    other.setRaces(TestResourceGenerator.generateRaceList(2));
    Assertions.assertNotEquals(event, other);

    // end dates not equal
    other.setEndDate(new Date());
    Assertions.assertNotEquals(event, other);

    // start dates not equal
    other.setStartDate(new Date());
    Assertions.assertNotEquals(event, other);

    // names not equal
    other.setName("not the same");
    Assertions.assertNotEquals(event, other);

    // ids not equal
    Assertions.assertNotEquals(new Event(), new Event());
  }

  @Test
  @DisplayName("Hash codes should be equal for identical objects")
  public void hashCode_shouldBeEqualForIdenticalObjects() {
    Event other = new Event(event);
    Assertions.assertEquals(event.hashCode(), other.hashCode());

    other.setIsActive(true);
    Assertions.assertNotEquals(event.hashCode(), other.hashCode());

    event.setIsActive(true);
    Assertions.assertEquals(event.hashCode(), other.hashCode());

    event.setRaces(Collections.emptyList());
    Assertions.assertNotEquals(event.hashCode(), other.hashCode());
  }

  @Test
  @DisplayName(
      "String representations should be equal when the objects are equal")
  public void
  toString_shouldBeEqualWhenObjectsAreSame() {
    Event other = new Event(event);

    Assertions.assertEquals(event.toString(), other.toString());
  }
}
