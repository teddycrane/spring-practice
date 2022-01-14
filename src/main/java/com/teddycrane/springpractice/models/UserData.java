package com.teddycrane.springpractice.models;

public class UserData {
  private String username = "";
  private String id = "";
  private String name = "";

  public UserData(String username, String id, String name) {
    this.username = username;
    this.id = id;
    this.name = name;
  }

  public String getUsername() {
    return username;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }
}
