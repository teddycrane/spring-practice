package com.teddycrane.springpractice.repository;

import com.teddycrane.springpractice.entity.Racer;
import com.teddycrane.springpractice.enums.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IRacerRepository extends CrudRepository<Racer, UUID>
{

	List<Racer> findByFirstNameAndLastName(String firstName, String lastName);

	Iterable<Racer> findByFirstNameContaining(String firstName);

	Iterable<Racer> findByLastNameContaining(String lastName);

	List<Racer> findByCategory(Category category);

	@Query(value = "SELECT race_id FROM race_racers WHERE racers_id = ?1", nativeQuery = true)
	List<UUID> findRacesWithRacer(UUID racerId);
}
