package com.innowise.smartlab.entity;

import java.time.LocalDateTime;

public class Match {

  private Team homeTeam;

  private Team awayTeam;

  //  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy hh:mm")
  public LocalDateTime conductionDateTime;

  public Match() {

  }

  public Match(Team homeTeam, Team awayTeam, LocalDateTime dateTim) {
    this.homeTeam = homeTeam;
    this.awayTeam = awayTeam;
    this.conductionDateTime = dateTim;
  }

  public Team getHomeTeam() {
    return homeTeam;
  }

  public void setHomeTeam(Team homeTeam) {
    this.homeTeam = homeTeam;
  }

  public Team getAwayTeam() {
    return awayTeam;
  }

  public void setAwayTeam(Team awayTeam) {
    this.awayTeam = awayTeam;
  }

  public LocalDateTime getConductionDateTime() {
    return conductionDateTime;
  }

  public void setConductionDateTime(LocalDateTime conductionDateTime) {
    this.conductionDateTime = conductionDateTime;
  }
}
