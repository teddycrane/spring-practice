package com.teddycrane.springpractice.repository;

import com.teddycrane.springpractice.entity.Event;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventRepository extends CrudRepository<Event, UUID> {

	Optional<Event> findByName(String name);
}
