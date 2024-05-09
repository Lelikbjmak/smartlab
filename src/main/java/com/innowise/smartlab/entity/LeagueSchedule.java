package com.innowise.smartlab.entity;

import java.util.List;

public class LeagueSchedule {

  String league;

  String country;

  List<Round> rounds;

  public LeagueSchedule() {
  }

  public LeagueSchedule(String league, String country, List<Round> rounds) {
    this.league = league;
    this.country = country;
    this.rounds = rounds;
  }

  public String getLeague() {
    return league;
  }

  public void setLeague(String league) {
    this.league = league;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public List<Round> getRounds() {
    return rounds;
  }

  public void setRounds(List<Round> rounds) {
    this.rounds = rounds;
  }
}
