package com.teddycrane.springpractice.service;

import com.teddycrane.springpractice.entity.Race;
import com.teddycrane.springpractice.enums.Category;
import com.teddycrane.springpractice.exceptions.DuplicateItemException;
import com.teddycrane.springpractice.exceptions.RaceNotFoundException;
import com.teddycrane.springpractice.exceptions.UpdateException;
import com.teddycrane.springpractice.helper.EnumHelpers;
import com.teddycrane.springpractice.repository.RaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RaceService implements IRaceService {

	@Autowired
	private RaceRepository raceRepository;

	@Override
	public List<Race> getAllRaces() {
		System.out.println("RaceService.getAllRaces called");
		Iterable<Race> response = raceRepository.findAll();
		List<Race> result = new ArrayList<>();
		response.forEach(result::add);
		return result;
	}

	@Override
	public Race getRace(UUID id) throws RaceNotFoundException {
		System.out.println("RaceService.getRace called");
		Optional<Race> result = this.raceRepository.findById(id);

		if (result.isPresent()) {
			return new Race(result.get());
		} else {
			System.out.println("Unable to find race");
			String message = String.format("Unable to find a race with id %s\n", id);
			throw new RaceNotFoundException(message);
		}
	}

	@Override
	public Race createRace(String name, Category category) throws DuplicateItemException {
		System.out.println("RaceService.createRace called");
		Optional<Race> existing = this.raceRepository.findByName(name);

		if (existing.isPresent()) {
			if (existing.get().getCategory() == category && existing.get().getName().equals(name)) {
				System.out.println("Name collision detected!");
				throw new DuplicateItemException(
						String.format("An event for category %s with name %s already exists!", EnumHelpers.getCategoryMapping(category), name));
			}
		}
		return this.raceRepository.save(new Race(name, category));
	}

	@Override
	public Race updateRace(UUID id, String name, Category category) throws UpdateException, RaceNotFoundException, DuplicateItemException {
		System.out.println("RaceService.updateRace called");
		Optional<Race> _race = this.raceRepository.findById(id);

		if (_race.isPresent()) {
			Race race = new Race(_race.get());

			if (name != null) {
				race.setName(name);
			}
			if (category != null) {
				race.setCategory(category);
			}

			Optional<Race> other = this.raceRepository.findByName(name);

			// name collision validation
			if (other.isPresent() && other.get().getName().equals(name) && other.get().getCategory() == category) {
				System.out.println("Duplicate item detected");
				throw new DuplicateItemException(String.format("An item with the name %s and category %s already exists!", name, EnumHelpers.getCategoryMapping(category)));
			}

			try {
				return this.raceRepository.save(race);
			} catch (Exception e) {
				throw new UpdateException(String.format("Unable to update race with id %s", id));
			}
		} else {
			System.out.println("Unable to find a race!");
			throw new RaceNotFoundException(String.format("Unable to find a race with id %s", id));
		}
	}
}
