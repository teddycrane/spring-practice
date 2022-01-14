package com.teddycrane.springpractice.event.request;

import javax.validation.constraints.NotEmpty;

public class SetResultRequest {

  @NotEmpty private final String[] ids;

  public SetResultRequest() {
    ids = null;
  }

  public SetResultRequest(String[] ids) {
    this.ids = ids;
  }

  public String[] getIds() {
    return ids;
  }
}
