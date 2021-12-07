package com.teddycrane.springpractice.controller.model;

import com.teddycrane.springpractice.entity.User;
import com.teddycrane.springpractice.exceptions.BadRequestException;
import com.teddycrane.springpractice.exceptions.DuplicateItemException;
import com.teddycrane.springpractice.exceptions.UserNotFoundError;
import com.teddycrane.springpractice.models.CreateUserRequest;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

@RequestMapping(path = "/users")
public interface IUserController
{

	@GetMapping(path = "/all")
	Collection<User> getAllUsers();

	@GetMapping
	User getUser(@RequestParam String id) throws BadRequestException, UserNotFoundError;

	@PostMapping
	User createUser(@RequestBody @Valid CreateUserRequest request) throws BadRequestException, DuplicateItemException;
}
