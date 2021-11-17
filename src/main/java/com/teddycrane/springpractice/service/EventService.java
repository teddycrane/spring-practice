package com.teddycrane.springpractice.service;

import com.teddycrane.springpractice.entity.Event;
import com.teddycrane.springpractice.entity.Race;
import com.teddycrane.springpractice.exceptions.DuplicateItemException;
import com.teddycrane.springpractice.exceptions.EventNotFoundException;
import com.teddycrane.springpractice.exceptions.RaceNotFoundException;
import com.teddycrane.springpractice.repository.EventRepository;
import com.teddycrane.springpractice.repository.RaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
class EventService implements IEventService
{

	@Autowired
	private EventRepository eventRepository;
	@Autowired
	private RaceRepository raceRepository;

	@Override
	public List<Event> getAllEvents()
	{
		System.out.println("EventService.getAllEvents called");
		List<Event> result = new ArrayList<>();
		Iterable<Event> response = this.eventRepository.findAll();
		response.forEach(result::add);
		return result;
	}

	@Override
	public Event getEvent(UUID id) throws EventNotFoundException
	{
		System.out.println("EventService.getEvent called");
		Optional<Event> response = this.eventRepository.findById(id);

		if (response.isPresent())
		{
			return response.get();
		} else
		{
			System.out.println("No event found!");
			throw new EventNotFoundException(String.format("No event exists for id %s", id));
		}
	}

	@Override
	public Event createEvent(String name, Date startDate, Date endDate) throws DuplicateItemException
	{
		System.out.println("EventService.createEvent called");
		Optional<Event> existing = this.eventRepository.findByName(name);
		Event e = new Event(name);

		if (existing.isPresent() && existing.get().getStartDate().equals(startDate) && existing.get().getEndDate().equals(endDate))
		{
			System.out.println("Duplicate item detected");
			throw new DuplicateItemException(String.format("An event with the name %s already exists! Try adding a race to this event instead. ", name));
		}

		if (startDate != null)
		{
			e.setStartDate(startDate);
			e.setEndDate(startDate);
		}
		if (endDate != null) e.setEndDate(endDate);

		return this.eventRepository.save(e);
	}

	@Override
	public Event deleteEvent(UUID id) throws EventNotFoundException
	{
		System.out.println("EventService.deleteEvent called");
		Optional<Event> existing = this.eventRepository.findById(id);
		Event e;

		if (existing.isPresent())
		{
			e = new Event(existing.get());
			this.eventRepository.delete(existing.get());
			return e;
		} else
		{
			throw new EventNotFoundException(String.format("No event found with id %s", id));
		}
	}

	@Override
	public Event addRacesToEvent(UUID id, List<UUID> raceIds) throws EventNotFoundException
	{
		System.out.println("EventService.addRacesToEvent called");
		Optional<Event> existing = this.eventRepository.findById(id);

		if (existing.isPresent())
		{
			Event e = new Event(existing.get());
			List<Race> raceList = new ArrayList<>(e.getRaces());
			Iterable<Race> races = this.raceRepository.findAllById(raceIds);

			// add races to list of races already present in event, and de-duplicate
			races.forEach((race) -> {
				if (!raceList.contains(race))
				{
					raceList.add(race);
				}
			});

			e.setRaces(raceList);
			return this.eventRepository.save(e);
		} else
		{
			throw new EventNotFoundException(String.format("No event found with id %s", id));
		}
	}

}
