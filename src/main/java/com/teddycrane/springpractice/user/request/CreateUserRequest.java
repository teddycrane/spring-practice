package com.teddycrane.springpractice.user.request;

import com.teddycrane.springpractice.enums.UserType;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import org.springframework.lang.Nullable;

public class CreateUserRequest {
  @NotNull private String username;

  @NotNull private String password;

  @NotNull private String firstName;

  @NotNull private String lastName;

  @NotNull private String email;

  // Can be null, defaults to basic user type (least-privileged)
  @Nullable private UserType type;

  public CreateUserRequest(String username, String password, String firstName,
                           String lastName, String email, UserType type) {
    this.username = username;
    this.password = password;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.type = type;
  }

  public String getFirstName() { return firstName; }

  public String getLastName() { return lastName; }

  public String getUsername() { return username; }

  public String getPassword() { return password; }

  public String getEmail() { return email; }

  public Optional<UserType> getType() { return Optional.ofNullable(type); }
}
