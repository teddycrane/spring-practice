package com.teddycrane.springpractice.user;

import com.teddycrane.springpractice.exceptions.BadRequestException;
import com.teddycrane.springpractice.exceptions.DuplicateItemException;
import com.teddycrane.springpractice.exceptions.UserNotFoundError;
import com.teddycrane.springpractice.models.BaseController;
import com.teddycrane.springpractice.user.model.IUserController;
import com.teddycrane.springpractice.user.model.IUserService;
import com.teddycrane.springpractice.user.request.CreateUserRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.UUID;

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

	@Override
	public User getUser(String id) throws BadRequestException, UserNotFoundError
	{
		logger.trace("getUser called");

		try
		{
			UUID requestId = UUID.fromString(id);
			return this.userService.getUser(requestId);
		} catch (IllegalArgumentException e)
		{
			logger.error("Unable to parse the id {}", id);
			throw new BadRequestException("Unable to parse the provided id");
		}
	}

	@Override
	public User createUser(CreateUserRequest request) throws BadRequestException, DuplicateItemException
	{
		logger.trace("createUser called");

		return this.userService.createUser(request.getFirstName(), request.getLastName(), request.getType());
	}
}
