package com.teddycrane.springpractice.controller;

import com.teddycrane.springpractice.controller.model.IGenerateController;
import com.teddycrane.springpractice.entity.Racer;
import com.teddycrane.springpractice.enums.Category;
import com.teddycrane.springpractice.exceptions.BadRequestException;
import com.teddycrane.springpractice.helper.EnumHelpers;
import com.teddycrane.springpractice.service.IGenerateService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/generate")
public class GenerateController extends BaseController implements IGenerateController
{

	private final IGenerateService generateService;

	public GenerateController(IGenerateService generateService)
	{
		super();
		this.generateService = generateService;
	}

	@Override
	public Racer generateSingleRacer(String category) throws BadRequestException
	{
		logger.trace("generateSingleRacer called");

		if (category != null)
		{
			// test if category is valid
			if (EnumHelpers.testEnumValue(Category.class, category))
			{
				return this.generateService.generateSingleRacer(Category.valueOf(category.toUpperCase()));
			} else {
				logger.error("The provided value {} is not a valid category value!", category);
				throw new BadRequestException("");
			}
		} else
		{
			return this.generateService.generateSingleRacer();
		}
	}
}
