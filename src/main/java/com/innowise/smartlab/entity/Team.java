package com.innowise.smartlab.entity;

import java.time.Year;
import java.util.Objects;

public class Team {

  private String name;

  private Year foundingDate;

  public Team() {

  }

  public Team(String name, Year foundingDate) {
    this.name = name;
    this.foundingDate = foundingDate;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Year getFoundingDate() {
    return foundingDate;
  }

  public void setFoundingDate(Year foundingDate) {
    this.foundingDate = foundingDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Team team = (Team) o;
    return Objects.equals(name, team.name) && Objects.equals(foundingDate,
        team.foundingDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, foundingDate);
  }
}
