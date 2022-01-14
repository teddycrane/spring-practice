package com.teddycrane.springpractice.models;

public class BaseSuccessResponse {

  private boolean success;

  public BaseSuccessResponse() {
    success = true;
  }

  public BaseSuccessResponse(boolean success) {
    this.success = success;
  }

  public boolean getSuccess() {
    return this.success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }
}
