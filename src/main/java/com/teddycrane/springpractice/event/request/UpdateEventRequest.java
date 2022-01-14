package com.teddycrane.springpractice.event.request;

import java.util.ArrayList;
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

  public UpdateEventRequest(@Nullable String name, @Nullable Date startDate,
                            @Nullable Date endDate, List<UUID> raceIds) {
    this.name = name;
    this.startDate = startDate;
    this.endDate = endDate;
    this.raceIds = new ArrayList<>(raceIds);
  }

  public Optional<String> getName() { return Optional.ofNullable(this.name); }

  public List<UUID> getRaceIds() { return raceIds; }

  public Optional<Date> getStartDate() {
    return Optional.ofNullable(this.startDate);
  }

  public Optional<Date> getEndDate() {
    return Optional.ofNullable(this.endDate);
  }
}
