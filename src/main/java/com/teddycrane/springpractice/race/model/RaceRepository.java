package com.teddycrane.springpractice.race.model;

import com.teddycrane.springpractice.enums.Category;
import com.teddycrane.springpractice.race.Race;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RaceRepository extends JpaRepository<Race, UUID> {

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
