package com.teddycrane.springpractice.service;

import com.teddycrane.springpractice.entity.Racer;
import com.teddycrane.springpractice.enums.Category;
import org.springframework.stereotype.Service;

@Service
public interface IGenerateService
{
	Racer generateSingleRacer();

	Racer generateSingleRacer(Category category);
}
