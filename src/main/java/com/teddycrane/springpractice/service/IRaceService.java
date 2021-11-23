package com.teddycrane.springpractice.service;

import com.teddycrane.springpractice.entity.Race;
import com.teddycrane.springpractice.enums.Category;
import com.teddycrane.springpractice.exceptions.DuplicateItemException;
import com.teddycrane.springpractice.exceptions.RaceNotFoundException;
import com.teddycrane.springpractice.exceptions.RacerNotFoundException;
import com.teddycrane.springpractice.exceptions.UpdateException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface IRaceService {

	List<Race> getAllRaces();

	Race getRace(UUID id) throws RaceNotFoundException;

	Race createRace(String name, Category category) throws DuplicateItemException;

	Race createRace(String name, Category category, Date startTime) throws DuplicateItemException;

	Race createRace(String name, Category category, Date startTime, Date endTime) throws DuplicateItemException;

	Race updateRace(UUID id, String name, Category category) throws UpdateException, RaceNotFoundException, DuplicateItemException;

	Race addRacer(UUID id, List<UUID> racerIds) throws RacerNotFoundException, RaceNotFoundException;
}
