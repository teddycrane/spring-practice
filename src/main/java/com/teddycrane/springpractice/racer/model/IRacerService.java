package com.teddycrane.springpractice.racer.model;

import com.teddycrane.springpractice.enums.Category;
import com.teddycrane.springpractice.enums.RacerFilterType;
import com.teddycrane.springpractice.error.BadRequestException;
import com.teddycrane.springpractice.error.RacerNotFoundException;
import com.teddycrane.springpractice.racer.Racer;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public interface IRacerService {

  /**
   * Gets all racers from DB
   *
   * @return a List of all the racers in the DB
   */
  List<Racer> getAllRacers();

  /**
   * Gets a racer by ID
   *
   * @param id The UUID of the racer to be found
   * @return An Optional object wrapping a Racer that isPresent if the racer is
   *     found.
   */
  Racer getRacerById(UUID id) throws RacerNotFoundException;

  Racer addRacer(String firstName, String lastName);

  Racer updateRacer(UUID id, String firstName, String lastName,
                    Category category) throws RacerNotFoundException;

  Racer deleteRacer(UUID id) throws RacerNotFoundException;

  List<Racer> getAllRacersWithDeleted();

  Racer restoreRacer(UUID id) throws RacerNotFoundException;

  List<Racer> getRacersByType(RacerFilterType filterType, String value)
      throws BadRequestException;
}
