package com.teddycrane.springpractice.service;

import com.teddycrane.springpractice.entity.Racer;
import com.teddycrane.springpractice.enums.Category;
import com.teddycrane.springpractice.exceptions.RacerNotFoundException;
import com.teddycrane.springpractice.repository.RacerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RacerService implements IRacerService {

	@Autowired
	private RacerRepository racerRepository;


	@Override
	public List<Racer> getAllRacers() {
		System.out.println("RacerService.getAllRacers called");
		List<Racer> allRacers = new ArrayList<>();
		racerRepository.findAll().forEach(allRacers::add);

		allRacers.removeIf(Racer::getIsDeleted);
		return allRacers;
	}

	@Override
	public Optional<Racer> getRacerById(UUID id) {
		System.out.println("RacerService.getRacerById called");
		return racerRepository.findById(id);
	}

	@Override
	public Racer addRacer(String firstName, String lastName) {
		System.out.println("RacerService.addRacer called");
		Racer r = new Racer(firstName, lastName);
		return racerRepository.save(r);
	}

	@Override
	public Racer updateRacer(UUID id, String firstName, String lastName, Category category) throws RacerNotFoundException {
		System.out.println("RacerService.updateRacer called");
		Optional<Racer> r = this.racerRepository.findById(id);
		Racer racer;

		if (r.isPresent()) {
			racer = new Racer(r.get());
			if (firstName != null) racer.setFirstName(firstName);
			if (lastName != null) racer.setLastName(lastName);
			if (category != null) racer.setCategory(category);

			return this.racerRepository.save(racer);
		} else {
			System.out.println("Unable to find a racer");
			throw new RacerNotFoundException("Unable to find the specified racer.");
		}
	}

	@Override
	public Racer deleteRacer(UUID id) throws RacerNotFoundException {
		Optional<Racer> r = this.racerRepository.findById(id);
		Racer racer;

		if (r.isPresent()) {
			racer = new Racer(r.get());
			racer.setIsDeleted(true);
			this.racerRepository.save(racer);
			return racer;
		} else {
			System.out.println("Unable to find the specified racer");
			throw new RacerNotFoundException("No racer found!");
		}
	}

}
