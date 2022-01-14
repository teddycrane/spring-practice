package com.teddycrane.springpractice.user.response;

import com.teddycrane.springpractice.models.BaseSuccessResponse;

public class PasswordResetResponse extends BaseSuccessResponse {
  private final String newPassword;

  public PasswordResetResponse() {
    super();
    this.newPassword = "";
  }

  public PasswordResetResponse(boolean status, String newPassword) {
    super(status);
    this.newPassword = newPassword;
  }

  public String getNewPassword() { return this.newPassword; }
}
