package com.teddycrane.springpractice.user.request;

import java.util.Optional;

import javax.validation.constraints.NotNull;

import com.teddycrane.springpractice.enums.UserType;

import org.springframework.lang.Nullable;

public class UpdateUserRequest {
    @NotNull
    private String userId;

    @Nullable
    private String firstName, lastName, password, username;

    @Nullable
    private UserType userType;

    public String getUserId()
    {
        return userId;
    }

    public Optional<String> getFirstName()
    {
        return Optional.ofNullable(firstName);
    }

    public Optional<String> getLastName()
    {
        return Optional.ofNullable(lastName);
    }

    public Optional<String> getPassword()
    {
        return Optional.ofNullable(password);
    }

    public Optional<String> getUsername()
    {
        return Optional.ofNullable(username);
    }

    public Optional<UserType> getUserType()
    {
        return Optional.ofNullable(userType);
    }

}
