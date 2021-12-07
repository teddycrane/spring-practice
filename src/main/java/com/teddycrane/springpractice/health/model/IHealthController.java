package com.teddycrane.springpractice.health.model;

import com.teddycrane.springpractice.health.Health;
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

