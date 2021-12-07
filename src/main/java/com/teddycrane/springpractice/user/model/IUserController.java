package com.teddycrane.springpractice.user.model;

import com.teddycrane.springpractice.user.User;
import com.teddycrane.springpractice.error.BadRequestException;
import com.teddycrane.springpractice.error.DuplicateItemException;
import com.teddycrane.springpractice.error.UserNotFoundError;
import com.teddycrane.springpractice.user.request.CreateUserRequest;
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
