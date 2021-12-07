package com.teddycrane.springpractice.controller;

import com.teddycrane.springpractice.controller.model.IUserController;
import com.teddycrane.springpractice.entity.User;
import com.teddycrane.springpractice.service.model.IUserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RequestMapping(path = "/users")
@RestController
public class UserController extends BaseController implements IUserController
{

	private final IUserService userService;

	public UserController(IUserService userService)
	{
		super();
		this.userService = userService;
	}

	@Override
	public Collection<User> getAllUsers()
	{
		logger.trace("getAllUsers called");

		// todo add auth here
		return this.userService.getAllUsers();
	}
}
