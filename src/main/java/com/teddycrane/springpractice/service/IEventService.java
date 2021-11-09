package com.teddycrane.springpractice.service;

import com.teddycrane.springpractice.entity.Event;
import com.teddycrane.springpractice.exceptions.DuplicateItemException;
import com.teddycrane.springpractice.exceptions.EventNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface IEventService {

	List<Event> getAllEvents();

	Event getEvent(UUID id) throws EventNotFoundException;

	Event createEvent(String name, Date startDate, Date endDate) throws DuplicateItemException;
}
