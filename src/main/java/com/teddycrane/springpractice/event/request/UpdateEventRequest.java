package com.teddycrane.springpractice.event.request;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.lang.Nullable;

public class UpdateEventRequest {

  @Nullable private String name;

  @Nullable private Date startDate;

  @Nullable private Date endDate;

  private List<UUID> raceIds;

  public Optional<String> getName() {
    return Optional.ofNullable(this.name);
  }

  public List<UUID> getRaceIds() {
    return raceIds;
  }

  public Optional<Date> getStartDate() {
    return Optional.ofNullable(this.startDate);
  }

  public Optional<Date> getEndDate() {
    return Optional.ofNullable(this.endDate);
  }
}
