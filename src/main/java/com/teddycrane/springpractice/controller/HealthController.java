package com.teddycrane.springpractice.controller;

import com.teddycrane.springpractice.controller.model.IHealthController;
import com.teddycrane.springpractice.models.Health;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/health")
public class HealthController extends BaseController implements IHealthController
{

	@Override
	public Health getHealth()
	{
		logger.trace("getHealth called");
		return new Health("health");
	}
}
