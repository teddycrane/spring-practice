package com.teddycrane.springpractice.user.model;

import com.teddycrane.springpractice.error.NotAuthenticatedException;
import com.teddycrane.springpractice.user.User;
import com.teddycrane.springpractice.error.BadRequestException;
import com.teddycrane.springpractice.error.DuplicateItemException;
import com.teddycrane.springpractice.error.UserNotFoundError;
import com.teddycrane.springpractice.user.request.AuthenticationRequest;
import com.teddycrane.springpractice.user.request.CreateUserRequest;
import com.teddycrane.springpractice.user.request.PasswordChangeRequest;
import com.teddycrane.springpractice.user.request.UpdateUserRequest;
import com.teddycrane.springpractice.user.response.AuthenticationResponse;
import com.teddycrane.springpractice.user.response.PasswordChangeResponse;
import com.teddycrane.springpractice.user.response.PasswordResetResponse;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

@RequestMapping(path = "/users")
public interface IUserController {

	@GetMapping(path = "/all")
	Collection<User> getAllUsers();

	@GetMapping(path = "/{id}")
	User getUser(@PathVariable String id) throws BadRequestException, UserNotFoundError;

	@PostMapping
	User createUser(@RequestBody @Valid CreateUserRequest request) throws BadRequestException, DuplicateItemException;

	@PostMapping(path = "/login")
	AuthenticationResponse login(@RequestBody @Valid AuthenticationRequest request)
			throws BadRequestException, NotAuthenticatedException, UserNotFoundError;

	@PatchMapping
	User updateUser(@RequestBody @Valid UpdateUserRequest request) throws BadRequestException, UserNotFoundError;

	// TODO update to be requestBody instead
	@PostMapping(path = "/reset-password")
	PasswordResetResponse resetPassword(@RequestParam String userId) throws BadRequestException, UserNotFoundError;

	@GetMapping(path = "/search-users")
	Collection<User> searchUsers(@RequestParam String searchType, @RequestParam String searchValue)
			throws BadRequestException;

	@PostMapping(path = "/change-password")
	PasswordChangeResponse changePassword(@RequestBody @Valid PasswordChangeRequest request,
			@RequestHeader("requester-id") String requesterId) throws BadRequestException, UserNotFoundError;

	@PostMapping(path = "/create-new")
	User createUserNoAuth(@RequestBody @Valid CreateUserRequest request)
			throws BadRequestException, DuplicateItemException;
}
