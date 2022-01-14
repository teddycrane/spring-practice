package com.teddycrane.springpractice.enums;

public enum RaceFilterType {
  CATEGORY("category"),
  NAME("name");

  private final String text;

  RaceFilterType(String text) { this.text = text; }

  public String toString() { return this.text; }
}
