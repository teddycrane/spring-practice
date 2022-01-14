package com.teddycrane.springpractice.enums;

public enum UserType {
  ROOT("root"),
  ADMIN("admin"),
  USER("user");

  private final String text;

  UserType(String text) { this.text = text; }

  public String toString() { return this.text; }
}
