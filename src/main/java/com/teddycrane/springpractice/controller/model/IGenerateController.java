package com.teddycrane.springpractice.controller.model;

import com.teddycrane.springpractice.entity.Racer;
import com.teddycrane.springpractice.exceptions.BadRequestException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/generate")
public interface IGenerateController
{
	@PostMapping(path = "/racer")
	Racer generateSingleRacer(@RequestParam(required = false) String category) throws BadRequestException;
}
