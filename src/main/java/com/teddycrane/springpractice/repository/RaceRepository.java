package com.teddycrane.springpractice.repository;

import com.teddycrane.springpractice.entity.Race;
import com.teddycrane.springpractice.enums.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RaceRepository extends CrudRepository<Race, UUID>
{

	Optional<Race> findByName(String name);

	/**
	 * Returns a list of Races that have the specified racer listed as a starter
	 *
	 * @param racerId The UUID of the Racer to be queried for
	 * @return A List of UUIDs of Races containing the specified Racer
	 */
	@Query(value = "SELECT race_id from race_racers WHERE racers_id = ?1", nativeQuery = true)
	List<UUID> findRacesWithRacer(@Param("id") String racerId);

	Collection<Race> findByCategory(Category category);

	Collection<Race> findByNameContaining(String name);
}
