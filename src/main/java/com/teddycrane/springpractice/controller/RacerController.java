package com.teddycrane.springpractice.controller;

import com.teddycrane.springpractice.entity.Racer;
import com.teddycrane.springpractice.enums.Category;
import com.teddycrane.springpractice.exceptions.CreationException;
import com.teddycrane.springpractice.exceptions.UpdateException;
import com.teddycrane.springpractice.models.CreateRacerRequest;
import com.teddycrane.springpractice.models.UpdateRacerRequest;
import com.teddycrane.springpractice.repository.RacerRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(path = "/racers")
public class RacerController {

	@Autowired
	private RacerRepository racerRepository;

	@GetMapping(path = "/all")
	public @ResponseBody
	Iterable<Racer> getAllRacers() {
		return racerRepository.findAll();
	}

	@PostMapping(path = "/new")
	public @ResponseBody
	Racer addRacer(@RequestBody @NotNull CreateRacerRequest request) throws CreationException {
		Racer r = new Racer();
		r.setCategory(Category.CAT_5);
		r.setFirstName(request.getFirstName());
		r.setLastName(request.getLastName());

		// duplication check
		try {
			List<Racer> existing = racerRepository.findByFirstNameAndLastName(r.getFirstName(), r.getLastName());
			Racer existingRacer = existing.get(0);
			if (existingRacer.getFirstName().equals(r.getFirstName()) && existingRacer.getLastName().equals(r.getLastName())) {
				throw new CreationException(String.format("Cannot create a new racer with name %s %s", request.getFirstName(), request.getLastName()));
			}
		} catch (IndexOutOfBoundsException ignored) {}
		racerRepository.save(r);
		return r;
	}

	@PutMapping(path = "/update")
	public @ResponseBody
	Racer updateRacer(@RequestBody @NotNull UpdateRacerRequest request) throws UpdateException {
		try {
			List<Racer> racers = racerRepository.findByFirstNameAndLastName(request.getFirstName(), request.getLastName());
			Racer existing = new Racer(racers.get(0));
			existing.setFirstName(request.getFirstName());
			existing.setLastName(request.getLastName());

			if (request.getCategory() != null) {
				existing.setCategory(request.getCategory());
			}
			return existing;
		} catch (IndexOutOfBoundsException e) {
			Racer r = new Racer(request.getFirstName(), request.getLastName());

			if (request.getCategory() != null) {
				r.setCategory(request.getCategory());
			}

			racerRepository.save(r);
			return r;
		} catch (Exception e) {
			throw new UpdateException(String.format("Unable to update rider with name %s %s", request.getFirstName(), request.getLastName()));
		}
	}
}
