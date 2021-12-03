package com.teddycrane.springpractice.controller;

import com.teddycrane.springpractice.models.Health;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/health")
public interface IHealthController
{
	@GetMapping
	Health getHealth();
}

