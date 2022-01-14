package com.teddycrane.springpractice.event;

import com.teddycrane.springpractice.error.BadRequestException;
import com.teddycrane.springpractice.error.DuplicateItemException;
import com.teddycrane.springpractice.error.EventNotFoundException;
import com.teddycrane.springpractice.event.model.IEventController;
import com.teddycrane.springpractice.event.model.IEventService;
import com.teddycrane.springpractice.event.request.CreateEventRequest;
import com.teddycrane.springpractice.event.request.UpdateEventRequest;
import com.teddycrane.springpractice.models.BaseController;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/events")
public class EventController
    extends BaseController implements IEventController {

  private final IEventService eventService;

  public EventController(IEventService eventService) {
    super();
    this.eventService = eventService;
  }

  @Override
  public List<Event> getAllEvents() {
    logger.trace("getAllEvents called");
    return this.eventService.getAllEvents();
  }

  @Override
  public Event getEvent(String eventId)
      throws BadRequestException, EventNotFoundException {
    logger.trace("getEvent called");

    try {
      UUID uuid = UUID.fromString(eventId);
      return this.eventService.getEvent(uuid);
    } catch (EventNotFoundException e) {
      throw new EventNotFoundException(e.getMessage());
    } catch (IllegalArgumentException e) {
      System.out.println("Bad id provided");
      throw new BadRequestException(String.format(
          "The id %s was provided in a format that was not readable. ",
          eventId));
    }
  }

  @Override
  public Event createEvent(CreateEventRequest request)
      throws DuplicateItemException, BadRequestException {
    this.logger.trace("createEvent called");

    try {
      if (request.getName() != null && (request.getStartDate().isPresent() ||
                                        request.getEndDate().isPresent())) {
        return this.eventService.createEvent(request.getName(),
                                             request.getStartDate().get(),
                                             request.getEndDate().get());
      } else {
        System.out.println(
            "Invalid request body.  Unable to create an event without a name and at least one of"
            + " [startDate, endDate]");
        throw new BadRequestException(
            "Unable to create an event without a name, and at least one of [startDate, endDate]!");
      }
    } catch (DuplicateItemException e) {
      throw new DuplicateItemException(e.getMessage());
    }
  }

  @Override
  public Event deleteEvent(String id)
      throws BadRequestException, EventNotFoundException {
    this.logger.trace("deleteEvent called");
    try {
      UUID eventId = UUID.fromString(id);
      return this.eventService.deleteEvent(eventId);
    } catch (IllegalArgumentException e) {
      this.logger.error(String.format("Unable to parse provided id %s", id), e);
      throw new BadRequestException(String.format(
          "Unable to handle id %s.  Please check the provided id and try again. ",
          id));
    } catch (EventNotFoundException e) {
      throw new EventNotFoundException(e.getMessage());
    }
  }

  @Override
  public Event addRacesToEvent(String eventId, UpdateEventRequest request)
      throws EventNotFoundException, BadRequestException {
    logger.trace("addRacesToEvent called");
    try {
      UUID id = UUID.fromString(eventId);
      if (request.getRaceIds().size() <= 0) {
        throw new BadRequestException("No race ids provided!");
      }
      return this.eventService.addRacesToEvent(id, request.getRaceIds());
    } catch (IllegalArgumentException e) {
      this.logger.trace("Bad UUID!", e);
      throw new BadRequestException(
          String.format("Unable to parse the id %s", eventId));
    } catch (EventNotFoundException e) {
      throw new EventNotFoundException(e.getMessage());
    }
  }

  @Override
  public Event startEvent(String eventId) throws EventNotFoundException {
    this.logger.trace("EventController.startEvent called");

    try {
      UUID id = UUID.fromString(eventId);
      return this.eventService.setEventAsActive(id, true);
    } catch (IllegalArgumentException e) {
      this.logger.error("Unable to parse id!", e);
      throw new BadRequestException(e.getMessage());
    } catch (EventNotFoundException e) {
      throw new EventNotFoundException(e.getMessage());
    }
  }

  @Override
  public Event endEvent(String eventId) throws EventNotFoundException {
    this.logger.trace("EventController.endEvent called");

    try {
      UUID id = UUID.fromString(eventId);
      return this.eventService.setEventAsActive(id, false);
    } catch (IllegalArgumentException e) {
      this.logger.error("Unable to parse id", e);
      throw new BadRequestException(e.getMessage());
    } catch (EventNotFoundException e) {
      throw new EventNotFoundException(e.getMessage());
    }
  }
}
