package com.teddycrane.springpractice.event.model;

import com.teddycrane.springpractice.error.DuplicateItemException;
import com.teddycrane.springpractice.error.EventNotFoundException;
import com.teddycrane.springpractice.event.Event;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public interface IEventService {

  List<Event> getAllEvents();

  Event getEvent(UUID id) throws EventNotFoundException;

  Event createEvent(String name, Date startDate, Date endDate)
      throws DuplicateItemException;

  Event deleteEvent(UUID id) throws EventNotFoundException;

  Event addRacesToEvent(UUID id, List<UUID> raceIds)
      throws EventNotFoundException;

  Event setEventAsActive(UUID id, boolean active) throws EventNotFoundException;
}
