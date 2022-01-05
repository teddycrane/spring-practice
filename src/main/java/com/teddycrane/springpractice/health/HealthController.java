package com.teddycrane.springpractice.health;

import com.teddycrane.springpractice.health.model.IHealthController;
import com.teddycrane.springpractice.models.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Health status endpoint
 */
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
