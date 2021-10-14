package com.teddycrane.springpractice.controller;

import com.teddycrane.springpractice.enums.Category;
import com.teddycrane.springpractice.model.Racer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RacerController {

	@GetMapping("/racer")
	public Racer getRacer(@RequestParam(name = "id") String id) {
		// mock DB connection
		return new Racer("firstName", "lastName_" + id, Category.CAT_1);
	}
}
