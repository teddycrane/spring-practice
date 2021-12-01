package com.teddycrane.springpractice.service;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class BaseService
{
	protected final Logger logger;

	public BaseService(Class<?> clazz)
	{
		this.logger = LogManager.getLogger(clazz);
	}
}
