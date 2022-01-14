package com.teddycrane.springpractice.enums;

public enum RacerFilterType {
  FIRSTNAME("firstname"),
  LASTNAME("lastname"),
  CATEGORY("category");

  private final String text;

  RacerFilterType(final String text) { this.text = text; }

  public String toString() { return this.text; }
}
