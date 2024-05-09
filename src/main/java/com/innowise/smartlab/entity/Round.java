package com.innowise.smartlab.entity;

import java.util.List;

public class Round {

  private int roundNumber;

  private List<Match> matches;

  public Round() {
  }

  public Round(int roundNumber, List<Match> matches) {
    this.roundNumber = roundNumber;
    this.matches = matches;
  }

  public int getRoundNumber() {
    return roundNumber;
  }

  public void setRoundNumber(int roundNumber) {
    this.roundNumber = roundNumber;
  }

  public List<Match> getMatches() {
    return matches;
  }

  public void setMatches(List<Match> matches) {
    this.matches = matches;
  }
}
