package com.teddycrane.springpractice.controller;

import com.teddycrane.springpractice.entity.Event;
import com.teddycrane.springpractice.exceptions.BadRequestException;
import com.teddycrane.springpractice.exceptions.DuplicateItemException;
import com.teddycrane.springpractice.exceptions.EventNotFoundException;
import com.teddycrane.springpractice.models.CreateEventRequest;
import com.teddycrane.springpractice.service.IEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/events")
public class EventController {

	@Autowired
	private IEventService eventService;

	@GetMapping(path = "/all")
	public List<Event> getAllEvents() {
		System.out.println("EventController.getAllEvents called");
		return this.eventService.getAllEvents();
	}

	@GetMapping
	public Event getEvent(@RequestParam String id) throws BadRequestException, EventNotFoundException {
		System.out.println("EventController.getEvent called");

		try {
			UUID uuid = UUID.fromString(id);
			return this.eventService.getEvent(uuid);
		} catch (EventNotFoundException e) {
			throw new EventNotFoundException(e.getMessage());
		} catch (IllegalArgumentException e) {
			System.out.println("Bad id provided");
			throw new BadRequestException(String.format("The id %s was provided in a format that was not readable. ", id));
		}
	}

	@PostMapping
	public Event createEvent(@RequestBody CreateEventRequest request) throws DuplicateItemException, BadRequestException {
		System.out.println("EventController.createEvent called");

		try {
			if (request.getName() != null && (request.getStartDate().isPresent() || request.getEndDate().isPresent())) {
				return this.eventService.createEvent(request.getName(), request.getStartDate().get(), request.getEndDate().get());
			} else {
				System.out.println("Invalid request body.  Unable to create an event without a name and at least one of [startDate, endDate]");
				throw new BadRequestException("Unable to create an event without a name, and at least one of [startDate, endDate]!");
			}
		} catch (DuplicateItemException e) {
			throw new DuplicateItemException(e.getMessage());
		}
	}

	@DeleteMapping
	public Event deleteEvent(@RequestParam String requestId) throws BadRequestException, EventNotFoundException {
		System.out.println("EventController.deleteEvent called");
		UUID id;

		try {
			id = UUID.fromString(requestId);
			return this.eventService.deleteEvent(id);
		} catch (IllegalArgumentException e) {
			System.out.printf("Unable to parse provided id %s", requestId);
			throw new BadRequestException(String.format("Unable to handle id %s.  Please check the provided id and try again. ", requestId));
		} catch (EventNotFoundException e) {
			throw new EventNotFoundException(e.getMessage());
		}
	}
}