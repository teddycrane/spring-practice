package com.teddycrane.springpractice.race.model;

import com.teddycrane.springpractice.error.*;
import com.teddycrane.springpractice.event.request.SetResultRequest;
import com.teddycrane.springpractice.race.Race;
import com.teddycrane.springpractice.race.request.CreateRaceRequest;
import com.teddycrane.springpractice.race.request.UpdateRaceRequest;
import com.teddycrane.springpractice.racer.request.AddRacerRequest;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RequestMapping(path = "/race")
public interface IRaceController {

  @GetMapping(path = "/{id}")
  Race getRace(@PathVariable String id) throws RaceNotFoundException, BadRequestException;

  @GetMapping(path = "/all")
  List<Race> getAllRaces() throws RaceNotFoundException;

  @PostMapping
  Race createRace(@RequestBody @Valid CreateRaceRequest request)
      throws BadRequestException, DuplicateItemException;

  @PatchMapping
  Race updateRace(@RequestBody @Valid UpdateRaceRequest request, @RequestParam String id)
      throws BadRequestException, RaceNotFoundException, UpdateException;

  @PatchMapping(path = "{raceId}/add-racer")
  Race addRacer(@RequestBody @Valid AddRacerRequest request, @PathVariable String raceId)
      throws BadRequestException, RaceNotFoundException, RacerNotFoundException, UpdateException;

  @PostMapping(path = "/{raceId}/start")
  Race startRace(@PathVariable String raceId)
      throws RaceNotFoundException, BadRequestException, StartException;

  @PostMapping(path = "/{raceId}/end")
  Race endRace(@PathVariable String raceId) throws RaceNotFoundException, BadRequestException;

  @PostMapping(path = "/{raceId}/place")
  Race setRacerResult(@PathVariable String raceId, @RequestBody @Valid SetResultRequest request)
      throws RaceNotFoundException, RacerNotFoundException, DuplicateItemException;

  @GetMapping(path = "/{raceId}/results")
  RaceResult getResults(@PathVariable String raceId)
      throws RaceNotFoundException, BadRequestException;

  @DeleteMapping(path = "/{raceId}")
  Race deleteRace(@PathVariable String raceId) throws BadRequestException, RaceNotFoundException;

  /**
   * This endpoint should be deprecated in favor a different URI
   *
   * @param racerId
   * @return
   * @throws BadRequestException
   * @throws RacerNotFoundException
   */
  @Deprecated
  @GetMapping(path = "/racer-results")
  Map<UUID, Integer> getResultsForRacer(@RequestParam String racerId)
      throws BadRequestException, RacerNotFoundException;

  @GetMapping(path = "/filter")
  Collection<Race> filterRaces(@RequestParam String type, @RequestParam String value)
      throws BadRequestException;
}
