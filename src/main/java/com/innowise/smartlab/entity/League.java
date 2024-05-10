package com.innowise.smartlab.entity;

import java.util.List;

public class League {

  private String league;

  private String country;

  private List<Team> teams;

  public League() {
  }

  public League(String league, String country, List<Team> teams) {
    this.league = league;
    this.country = country;
    this.teams = teams;
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

  public List<Team> getTeams() {
    return teams;
  }

  public void setTeams(List<Team> teams) {
    this.teams = teams;
  }
}
