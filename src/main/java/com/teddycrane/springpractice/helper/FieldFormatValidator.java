package com.teddycrane.springpractice.helper;

public class FieldFormatValidator {

  public static boolean isValidEmail(String email) {
    return email.matches("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
  }
}
