package com.teddycrane.springpractice.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

abstract class BaseController
{
	protected final Logger logger;

	public BaseController()
	{
		this.logger = LogManager.getLogger(this.getClass());
	}
}
