package com.teddycrane.springpractice.service;

import com.teddycrane.springpractice.entity.Racer;
import com.teddycrane.springpractice.repository.RacerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RacerService {

	@Autowired
	private RacerRepository racerRepository;


	public List<Racer> getAllRacers() {
		System.out.println("RacerService.getAllRacers called");
		List<Racer> allRacers = new ArrayList<>();
		racerRepository.findAll().forEach(allRacers::add);

		allRacers.removeIf(Racer::getIsDeleted);
		return allRacers;
	}
}
