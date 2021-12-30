package com.teddycrane.springpractice.user.request;

import java.util.Optional;

import javax.validation.constraints.NotNull;

import com.teddycrane.springpractice.enums.UserType;
import com.teddycrane.springpractice.helper.FieldFormatValidator;

import org.springframework.lang.Nullable;

public class UpdateUserRequest {
    @NotNull
    private String userId;

    @Nullable
    private String firstName, lastName, password, username, email;

    @Nullable
    private UserType userType;

    public UpdateUserRequest(String userId, String firstName, String lastName, String password, String username,
            String email, UserType userType) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.username = username;
        this.email = FieldFormatValidator.isValidEmail(email) ? email : "";
        this.userType = userType;
    }

    public String getUserId() {
        return userId;
    }

    public Optional<String> getFirstName() {
        return Optional.ofNullable(firstName);
    }

    public Optional<String> getLastName() {
        return Optional.ofNullable(lastName);
    }

    public Optional<String> getPassword() {
        return Optional.ofNullable(password);
    }

    public Optional<String> getUsername() {
        return Optional.ofNullable(username);
    }

    public Optional<String> getEmail() {
        return Optional.ofNullable(email);
    }

    public Optional<UserType> getUserType() {
        return Optional.ofNullable(userType);
    }

    @Override
    public boolean equals(Object other) {
        if (other.getClass().equals(this.getClass())) {
            UpdateUserRequest otherRequest = (UpdateUserRequest) other;
            return this.equal(otherRequest);
        }
        return false;
    }

    private boolean equal(UpdateUserRequest other) {
        return this.userId.equals(other.userId) &&
                this.firstName.equals(other.firstName) &&
                this.lastName.equals(other.lastName) &&
                this.username.equals(other.username) &&
                this.password.equals(other.password) &&
                this.userType.equals(other.userType);
    }

}
