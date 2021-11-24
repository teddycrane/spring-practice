package com.teddycrane.springpractice.tests.controllertests;

import com.teddycrane.springpractice.controller.IEventController;
import com.teddycrane.springpractice.service.IEventService;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EventControllerTest
{
	private IEventController eventController;
	private IEventService eventService;

	@Before
	public void init()
	{
	}
}
