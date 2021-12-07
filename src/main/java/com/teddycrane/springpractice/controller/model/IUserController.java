package com.teddycrane.springpractice.controller.model;

import com.teddycrane.springpractice.entity.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;

@RequestMapping(path = "/users")
public interface IUserController
{
	@GetMapping(path = "/all")
	Collection<User> getAllUsers();
}
