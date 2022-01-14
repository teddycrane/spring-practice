package com.teddycrane.springpractice.racer.request;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.validation.constraints.NotEmpty;

public class AddRacerRequest {

  @NotEmpty private List<UUID> racerIds;

  public AddRacerRequest() {
    racerIds = new ArrayList<>();
  }

  public AddRacerRequest(List<UUID> racerIds) {
    this.racerIds = new ArrayList<>(racerIds);
  }

  public List<UUID> getRacerIds() {
    return new ArrayList<>(this.racerIds);
  }
}
