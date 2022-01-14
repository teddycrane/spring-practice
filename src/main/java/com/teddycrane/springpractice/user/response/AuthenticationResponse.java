package com.teddycrane.springpractice.user.response;

public class AuthenticationResponse {

  private boolean authenticated;
  private String token;

  public AuthenticationResponse() {
    this.authenticated = true;
    this.token = "";
  }

  public AuthenticationResponse(boolean authenticated, String token) {
    this.authenticated = authenticated;
    this.token = token;
  }

  public boolean isAuthenticated() { return authenticated; }

  public void setAuthenticated(boolean authenticated) {
    this.authenticated = authenticated;
  }

  public String getToken() { return this.token; }

  public void setToken(String token) { this.token = token; }

  @Override
  public boolean equals(Object other) {
    if (other.getClass().equals(this.getClass())) {
      AuthenticationResponse response = (AuthenticationResponse)other;
      return response.isAuthenticated() == this.isAuthenticated() &&
          response.getToken().equals(this.token);
    }
    return false;
  }
}
