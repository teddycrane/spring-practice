package com.teddycrane.springpractice.service;

import com.teddycrane.springpractice.entity.Race;
import com.teddycrane.springpractice.enums.Category;
import com.teddycrane.springpractice.exceptions.*;
import com.teddycrane.springpractice.models.RaceResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public interface IRaceService {

	List<Race> getAllRaces();

	Race getRace(UUID id) throws RaceNotFoundException;

	Race createRace(String name, Category category) throws DuplicateItemException;

	Race createRace(String name, Category category, Date startTime) throws DuplicateItemException;

	Race createRace(String name, Category category, Date startTime, Date endTime) throws DuplicateItemException;

	Race updateRace(UUID id, String name, Category category) throws UpdateException, RaceNotFoundException, DuplicateItemException;

	Race addRacer(UUID id, List<UUID> racerIds) throws RacerNotFoundException, RaceNotFoundException;

	Race startRace(UUID id) throws RaceNotFoundException, StartException;

	Race endRace(UUID id) throws RaceNotFoundException, EndException;

	Race placeRacersInFinishOrder(UUID raceId, ArrayList<UUID> requestIds) throws RaceNotFoundException, RacerNotFoundException, DuplicateItemException, StartException;

	RaceResult getResults(UUID raceId) throws RaceNotFoundException;
}
