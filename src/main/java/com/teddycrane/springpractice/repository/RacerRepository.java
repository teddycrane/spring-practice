package com.teddycrane.springpractice.repository;

import com.teddycrane.springpractice.entity.Racer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RacerRepository extends CrudRepository<Racer, UUID> {
	List<Racer> findByFirstNameAndLastName(String firstName, String lastName);

}
