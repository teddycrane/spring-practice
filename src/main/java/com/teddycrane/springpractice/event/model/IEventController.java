package com.teddycrane.springpractice.event.model;

import com.teddycrane.springpractice.event.request.CreateEventRequest;
import com.teddycrane.springpractice.event.Event;
import com.teddycrane.springpractice.event.request.UpdateEventRequest;
import com.teddycrane.springpractice.error.BadRequestException;
import com.teddycrane.springpractice.error.DuplicateItemException;
import com.teddycrane.springpractice.error.EventNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/events")
public interface IEventController
{

	@GetMapping(path = "/all")
	List<Event> getAllEvents();

	@GetMapping
	Event getEvent(@RequestParam String eventId) throws BadRequestException, EventNotFoundException;

	@PostMapping
	Event createEvent(@RequestBody @Valid CreateEventRequest request) throws DuplicateItemException, BadRequestException;

	@DeleteMapping
	Event deleteEvent(@RequestParam String eventId) throws BadRequestException, EventNotFoundException;

	@PatchMapping(path = "/add-races")
	Event addRacesToEvent(@RequestParam String eventId, @RequestBody UpdateEventRequest request) throws EventNotFoundException, BadRequestException;

	@PostMapping(path = "/start-event")
	Event startEvent(@RequestParam String eventId) throws EventNotFoundException;

	@PostMapping(path = "/end-event")
	Event endEvent(@RequestParam String eventId) throws EventNotFoundException;
}
