package com.teddycrane.springpractice.enums;

public enum UserSearchType {
  TYPE("type"),
  STATUS("status"),
  USERNAME("username"),
  FULLNAME("fullname");

  private final String text;

  UserSearchType(final String text) {
    this.text = text;
  }

  @Override
  public String toString() {
    return this.text;
  }
}
