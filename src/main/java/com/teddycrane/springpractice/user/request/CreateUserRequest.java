package com.teddycrane.springpractice.user.request;

import com.teddycrane.springpractice.enums.UserType;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public class CreateUserRequest
{
	@NotNull
	private String userName;

	@NotNull
	private String password;

	@NotNull
	private String firstName;

	@NotNull
	private String lastName;

	// Can be null, defaults to basic user type (least-privileged)
	@Nullable
	private UserType type;

	public String getFirstName()
	{
		return firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public String getUserName()
	{
		return userName;
	}

	public String getPassword()
	{
		return password;
	}

	public Optional<UserType> getType()
	{
		return Optional.ofNullable(type);
	}
}
