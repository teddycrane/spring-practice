package com.teddycrane.springpractice.controller;

import com.teddycrane.springpractice.entity.Event;
import com.teddycrane.springpractice.exceptions.BadRequestException;
import com.teddycrane.springpractice.exceptions.DuplicateItemException;
import com.teddycrane.springpractice.exceptions.EventNotFoundException;
import com.teddycrane.springpractice.models.CreateEventRequest;
import com.teddycrane.springpractice.models.UpdateEventRequest;
import com.teddycrane.springpractice.service.IEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/events")
class EventController implements IEventController
{

	private final IEventService eventService;

	public EventController(IEventService eventService)
	{
		this.eventService = eventService;
	}

	@Override
	public List<Event> getAllEvents()
	{
		System.out.println("EventController.getAllEvents called");
		return this.eventService.getAllEvents();
	}

	@Override
	public Event getEvent(String id) throws BadRequestException, EventNotFoundException
	{
		System.out.println("EventController.getEvent called");

		try
		{
			UUID uuid = UUID.fromString(id);
			return this.eventService.getEvent(uuid);
		} catch (EventNotFoundException e)
		{
			throw new EventNotFoundException(e.getMessage());
		} catch (IllegalArgumentException e)
		{
			System.out.println("Bad id provided");
			throw new BadRequestException(String.format("The id %s was provided in a format that was not readable. ", id));
		}
	}

	@Override
	public Event createEvent(CreateEventRequest request) throws DuplicateItemException, BadRequestException
	{
		System.out.println("EventController.createEvent called");

		try
		{
			if (request.getName() != null && (request.getStartDate().isPresent() || request.getEndDate().isPresent()))
			{
				return this.eventService.createEvent(request.getName(), request.getStartDate().get(), request.getEndDate().get());
			} else
			{
				System.out.println("Invalid request body.  Unable to create an event without a name and at least one of [startDate, endDate]");
				throw new BadRequestException("Unable to create an event without a name, and at least one of [startDate, endDate]!");
			}
		} catch (DuplicateItemException e)
		{
			throw new DuplicateItemException(e.getMessage());
		}
	}

	@Override
	public Event deleteEvent(String requestId) throws BadRequestException, EventNotFoundException
	{
		System.out.println("EventController.deleteEvent called");
		UUID id;

		try
		{
			id = UUID.fromString(requestId);
			return this.eventService.deleteEvent(id);
		} catch (IllegalArgumentException e)
		{
			System.out.printf("Unable to parse provided id %s", requestId);
			throw new BadRequestException(String.format("Unable to handle id %s.  Please check the provided id and try again. ", requestId));
		} catch (EventNotFoundException e)
		{
			throw new EventNotFoundException(e.getMessage());
		}
	}

	@Override
	public Event addRacesToEvent(String requestId, UpdateEventRequest request) throws EventNotFoundException, BadRequestException
	{
		System.out.println("EventController.addRacesToEvent called");
		try
		{
			UUID id = UUID.fromString(requestId);
			if (request.getRaceIds().size() <= 0)
			{
				throw new BadRequestException("No race ids provided!");
			}
			return this.eventService.addRacesToEvent(id, request.getRaceIds());
		} catch (IllegalArgumentException e)
		{
			System.out.println("Bad UUID!");
			throw new BadRequestException(String.format("Unable to parse the id %s", requestId));
		} catch (EventNotFoundException e)
		{
			throw new EventNotFoundException(e.getMessage());
		}
	}

}
