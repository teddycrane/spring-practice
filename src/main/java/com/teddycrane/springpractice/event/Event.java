package com.teddycrane.springpractice.event;

import com.google.gson.Gson;
import com.teddycrane.springpractice.race.Race;
import java.util.*;
import javax.persistence.*;
import org.hibernate.annotations.Type;

@Entity
public class Event {

  @Id
  @Type(type = "uuid-char")
  @GeneratedValue(strategy = GenerationType.AUTO)
  private final UUID id;

  private boolean isActive;

  @Column(unique = true)
  private String name;

  @OneToMany private List<Race> races;
  private Date startDate, endDate;

  public Event() {
    this.id = UUID.randomUUID();
    this.races = new ArrayList<>();
    this.startDate = new Date();
    this.endDate = new Date();
    this.name = "";
    this.isActive = false;
  }

  public Event(String name) {
    this();
    this.name = name;
  }

  public Event(String name, Date startDate, Date endDate) {
    this(name);
    this.startDate = new Date(startDate.getTime());
    this.endDate = new Date(endDate.getTime());
  }

  public Event(String name, Date startDate) {
    this(name, startDate, startDate);
  }

  public Event(Event other) {
    this.id = other.id;
    this.name = other.name;
    this.races = new ArrayList<>(other.races);
    this.startDate = new Date(other.startDate.getTime());
    this.endDate = new Date(other.endDate.getTime());
    this.isActive = other.isActive;
  }

  public UUID getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Collection<Race> getRaces() {
    return new ArrayList<>(this.races);
  }

  public void setRaces(Collection<Race> races) {
    this.races = new ArrayList<>(races);
  }

  public Date getStartDate() {
    return new Date(startDate.getTime());
  }

  public void setStartDate(Date startDate) {
    this.startDate = new Date(startDate.getTime());
  }

  public Date getEndDate() {
    return new Date(endDate.getTime());
  }

  public void setEndDate(Date endDate) {
    this.endDate = new Date(endDate.getTime());
  }

  public boolean getIsActive() {
    return isActive;
  }

  public void setIsActive(boolean isActive) {
    this.isActive = isActive;
  }

  public boolean equals(Event other) {
    return this.id.equals(other.id)
        && this.name.equals(other.name)
        && this.startDate.equals(other.startDate)
        && this.endDate.equals(other.endDate)
        && this.races.equals(other.races)
        && this.isActive == other.isActive;
  }

  public String toString() {
    Gson gson = new Gson();
    return gson.toJson(this);
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 31 * hash + this.id.hashCode();
    hash = 31 * hash + this.name.hashCode();
    hash = 31 * hash + this.startDate.hashCode();
    hash = 31 * hash + this.endDate.hashCode();
    hash = this.isActive ? hash + (31) : hash;

    if (this.races.size() > 0) {
      for (Race race : races) {
        hash = 31 * hash + race.hashCode();
      }
    }
    return hash;
  }
}
