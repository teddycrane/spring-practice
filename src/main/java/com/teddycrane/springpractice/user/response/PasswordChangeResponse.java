package com.teddycrane.springpractice.user.response;

public class PasswordChangeResponse {
  private boolean success;

  private String username;

  public PasswordChangeResponse(boolean success, String username) {
    this.success = success;
    this.username = username;
  }

  public boolean isSuccess() {
    return success;
  }

  public String getUsername() {
    return username;
  }
}
