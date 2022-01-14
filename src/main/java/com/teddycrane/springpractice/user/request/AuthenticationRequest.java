package com.teddycrane.springpractice.user.request;

import java.util.Optional;
import javax.validation.constraints.NotNull;
import org.springframework.lang.Nullable;

public class AuthenticationRequest {

  @NotNull private String password;

  // we can login by either the username or the email
  @Nullable private String username, email;

  public AuthenticationRequest(@Nullable String username, @Nullable String email, String password) {
    this.email = email;
    this.password = password;
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public Optional<String> getUsername() {
    return Optional.ofNullable(username);
  }

  public Optional<String> getEmail() {
    return Optional.ofNullable(email);
  }
}
