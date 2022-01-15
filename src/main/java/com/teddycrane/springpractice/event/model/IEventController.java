package com.teddycrane.springpractice.event.model;

import com.teddycrane.springpractice.error.BadRequestException;
import com.teddycrane.springpractice.error.DuplicateItemException;
import com.teddycrane.springpractice.error.EventNotFoundException;
import com.teddycrane.springpractice.event.Event;
import com.teddycrane.springpractice.event.request.CreateEventRequest;
import com.teddycrane.springpractice.event.request.UpdateEventRequest;
import java.util.List;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/events")
public interface IEventController {

  @GetMapping(path = "/all") List<Event> getAllEvents();

  @GetMapping(path = "/{eventId}")
  Event getEvent(@PathVariable String eventId)
      throws BadRequestException, EventNotFoundException;

  @PostMapping
  Event createEvent(@RequestBody @Valid CreateEventRequest request)
      throws DuplicateItemException, BadRequestException;

  @DeleteMapping(path = "/{eventId}")
  Event deleteEvent(@PathVariable String eventId)
      throws BadRequestException, EventNotFoundException;

  @PatchMapping(path = "/{eventId}/add-races")
  Event addRacesToEvent(@PathVariable String eventId,
                        @RequestBody UpdateEventRequest request)
      throws EventNotFoundException, BadRequestException;

  @PostMapping(path = "/{eventId}/start-event")
  Event startEvent(@PathVariable String eventId) throws EventNotFoundException;

  @PostMapping(path = "/{eventId}/end-event")
  Event endEvent(@PathVariable String eventId) throws EventNotFoundException;
}
