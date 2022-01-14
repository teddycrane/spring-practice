package com.teddycrane.springpractice.event;

import com.teddycrane.springpractice.error.DuplicateItemException;
import com.teddycrane.springpractice.error.EventNotFoundException;
import com.teddycrane.springpractice.event.model.EventRepository;
import com.teddycrane.springpractice.event.model.IEventService;
import com.teddycrane.springpractice.models.BaseService;
import com.teddycrane.springpractice.race.Race;
import com.teddycrane.springpractice.race.model.RaceRepository;
import java.util.*;
import org.springframework.stereotype.Service;

@Service
public class EventService extends BaseService implements IEventService {

  private final EventRepository eventRepository;
  private final RaceRepository raceRepository;

  public EventService(EventRepository eventRepository,
                      RaceRepository raceRepository) {
    super();
    this.eventRepository = eventRepository;
    this.raceRepository = raceRepository;
  }

  @Override
  public List<Event> getAllEvents() {
    this.logger.trace("EventService.getAllEvents called");
    List<Event> result = new ArrayList<>();
    Iterable<Event> response = this.eventRepository.findAll();
    response.forEach(result::add);
    return result;
  }

  @Override
  public Event getEvent(UUID id) throws EventNotFoundException {
    this.logger.trace("EventService.getEvent called");
    Optional<Event> response = this.eventRepository.findById(id);

    if (response.isPresent()) {
      return response.get();
    } else {
      this.logger.error("No event found!");
      throw new EventNotFoundException(
          String.format("No event exists for id %s", id));
    }
  }

  @Override
  public Event createEvent(String name, Date startDate, Date endDate)
      throws DuplicateItemException {
    this.logger.trace("EventService.createEvent called");
    Optional<Event> existing = this.eventRepository.findByName(name);
    Event e = new Event(name);

    if (existing.isPresent() &&
        existing.get().getStartDate().equals(startDate) &&
        existing.get().getEndDate().equals(endDate)) {
      this.logger.error("Duplicate item detected for name {}", name);
      throw new DuplicateItemException(String.format(
          "An event with the name %s already exists! Try adding a race to this event instead. ",
          name));
    }

    if (startDate != null) {
      e.setStartDate(startDate);
      e.setEndDate(startDate);
    }
    if (endDate != null)
      e.setEndDate(endDate);

    return this.eventRepository.save(e);
  }

  @Override
  public Event deleteEvent(UUID id) throws EventNotFoundException {
    this.logger.trace("EventService.deleteEvent called");
    Optional<Event> existing = this.eventRepository.findById(id);
    Event e;

    if (existing.isPresent()) {
      e = new Event(existing.get());
      this.eventRepository.delete(existing.get());
      return e;
    } else {
      this.logger.error("Event Service was unable to find an event!");
      throw new EventNotFoundException(
          String.format("No event found with id %s", id));
    }
  }

  @Override
  public Event addRacesToEvent(UUID id, List<UUID> raceIds)
      throws EventNotFoundException {
    this.logger.trace("EventService.addRacesToEvent called");
    Optional<Event> existing = this.eventRepository.findById(id);

    if (existing.isPresent()) {
      Event e = new Event(existing.get());
      List<Race> raceList = new ArrayList<>(e.getRaces());
      Iterable<Race> races = this.raceRepository.findAllById(raceIds);

      // add races to list of races already present in event, and de-duplicate
      races.forEach((race) -> {
        if (!raceList.contains(race)) {
          raceList.add(race);
        }
      });

      e.setRaces(raceList);
      return this.eventRepository.save(e);
    } else {
      this.logger.error("The event service was unable to find an event!");
      throw new EventNotFoundException(
          String.format("No event found with id %s", id));
    }
  }

  @Override
  public Event setEventAsActive(UUID id, boolean active)
      throws EventNotFoundException {
    this.logger.trace("EventService.setEventAsActive called");
    Optional<Event> event = this.eventRepository.findById(id);

    if (event.isPresent()) {
      Event e = new Event(event.get());
      e.setIsActive(true);
      return this.eventRepository.save(e);
    } else {
      this.logger.error("The event service was unable to find the event!");
      throw new EventNotFoundException(
          String.format("No event found with the id %s", id));
    }
  }
}
