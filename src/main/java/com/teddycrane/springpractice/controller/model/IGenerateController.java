package com.teddycrane.springpractice.controller.model;

import com.teddycrane.springpractice.entity.Racer;
import com.teddycrane.springpractice.exceptions.BadRequestException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;

@RequestMapping(path = "/generate")
public interface IGenerateController
{

	@PostMapping(path = "/racer")
	Collection<Racer> generateRacer(@RequestParam(required = false) String category, @RequestParam(required = false) Integer number) throws BadRequestException;
}
