package com.teddycrane.springpractice.controller;

import com.teddycrane.springpractice.model.Health;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

	@GetMapping("/health")
	public Health getHealth() {
		return new Health();
	}
}
