package com.teddycrane.springpractice.enums;

public enum UserStatus {
  ACTIVE("ACTIVE"),
  PASSWORDCHANGEREQUIRED("PASSWORD_CHANGE_REQUIRED"),
  DISABLED("DISABLED");

  private final String message;

  UserStatus(final String message) {
    this.message = message;
  }

  public String toString() {
    return this.message;
  }
}
