package com.teddycrane.springpractice.user.request;

import javax.validation.constraints.NotNull;

public class PasswordChangeRequest {
  @NotNull private String userId;
  @NotNull private String oldPassword;
  @NotNull private String newPassword;

  public String getOldPassword() { return oldPassword; }

  public String getNewPassword() { return newPassword; }

  public String getUserId() { return userId; }
}
