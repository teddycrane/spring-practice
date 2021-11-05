package com.teddycrane.springpractice.service;

import com.teddycrane.springpractice.entity.Event;
import com.teddycrane.springpractice.exceptions.DuplicateItemException;
import com.teddycrane.springpractice.exceptions.EventNotFoundException;
import com.teddycrane.springpractice.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EventService implements IEventService {

	@Autowired
	private EventRepository eventRepository;

	@Override
	public List<Event> getAllEvents() {
		System.out.println("EventService.getAllEvents called");
		List<Event> result = new ArrayList<>();
		Iterable<Event> response = this.eventRepository.findAll();
		response.forEach(result::add);
		return result;
	}

	@Override
	public Event getEvent(UUID id) throws EventNotFoundException {
		System.out.println("EventService.getEvent called");
		Optional<Event> response = this.eventRepository.findById(id);

		if (response.isPresent()) {
			return response.get();
		} else {
			System.out.println("No event found!");
			throw new EventNotFoundException(String.format("No event exists for id %s", id));
		}
	}

	@Override
	public Event createEvent(String name, Date startDate, Date endDate) throws DuplicateItemException {
		System.out.println("EventService.createEvent called");
		Optional<Event> existing = this.eventRepository.findByName(name);
		Event e = new Event(name);

		if (existing.isPresent() && existing.get().getStartDate().equals(startDate) && existing.get().getEndDate().equals(endDate)) {
			System.out.println("Duplicate item detected");
			throw new DuplicateItemException(String.format("An event with the name %s already exists! Try adding a race to this event instead. ", name));
		}

		if (startDate != null) {
			e.setStartDate(startDate);
			e.setEndDate(startDate);
		}
		if (endDate != null) e.setEndDate(endDate);

		return this.eventRepository.save(e);
	}
}
