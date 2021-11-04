package com.teddycrane.springpractice.repository;

import com.teddycrane.springpractice.entity.Race;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RaceRepository extends CrudRepository<Race, UUID> {
	Optional<Race> findByName(String name);
}
