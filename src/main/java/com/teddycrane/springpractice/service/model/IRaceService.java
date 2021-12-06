package com.teddycrane.springpractice.service.model;

import com.teddycrane.springpractice.entity.Race;
import com.teddycrane.springpractice.enums.Category;
import com.teddycrane.springpractice.exceptions.*;
import com.teddycrane.springpractice.models.RaceResult;
import org.springframework.stereotype.Service;

import java.util.*;

@Service public interface IRaceService {

	List<Race> getAllRaces();

	Race getRace(UUID id) throws RaceNotFoundException;

	Race createRace(String name, Category category) throws DuplicateItemException;

	Race createRace(String name, Category category, Date startTime) throws DuplicateItemException;

	Race createRace(String name, Category category, Date startTime, Date endTime) throws DuplicateItemException;

	Race updateRace(UUID id, String name, Category category) throws UpdateException, RaceNotFoundException, DuplicateItemException;

	Race addRacer(UUID id, List<UUID> racerIds) throws RacerNotFoundException, RaceNotFoundException, UpdateException;

	Race startRace(UUID id) throws RaceNotFoundException, StartException;

	Race endRace(UUID id) throws RaceNotFoundException, EndException;

	Race placeRacersInFinishOrder(UUID raceId, List<UUID> requestIds) throws RaceNotFoundException, RacerNotFoundException, DuplicateItemException, StartException;

	RaceResult getResults(UUID raceId) throws RaceNotFoundException;

	Race deleteRace(UUID raceId) throws RaceNotFoundException;

	Map<UUID, Integer> getResultsForRacer(UUID id) throws RacerNotFoundException;
}
