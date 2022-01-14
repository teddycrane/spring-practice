package com.teddycrane.springpractice.event.model;

import com.teddycrane.springpractice.event.Event;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {

  Optional<Event> findByName(String name);
}
