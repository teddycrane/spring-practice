package com.teddycrane.springpractice.enums;

public enum Category {
  CAT1("CAT1"),
  CAT2("CAT2"),
  CAT3("CAT3"),
  CAT4("CAT4"),
  CAT5("CAT5");

  private final String text;

  Category(final String text) { this.text = text; }

  @Override
  public String toString() {
    return this.text;
  }
}
