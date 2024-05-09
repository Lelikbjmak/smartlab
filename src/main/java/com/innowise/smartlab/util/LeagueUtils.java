package com.innowise.smartlab.util;

import com.innowise.smartlab.entity.Match;


public class LeagueUtils {

  private static final int FIRST_TEAM_INDEX = 0;

  public static Match transposeMatch(Match match) {
    return new Match(match.getAwayTeam(), match.getHomeTeam(), match.getConductionDateTime());
  }

  public static int calculateHomeTeamIndex(int teamCount, int round, int match) {
    return (round + match) % (teamCount - 1);
  }

  public static int calculateAwayTeamIndex(int teamCount, int round, int match) {
    return match == FIRST_TEAM_INDEX ? teamCount - 1
        : (teamCount - 1 - match + round) % (teamCount - 1);
  }

  public static int computeTeamPairsNumber(int totalMatchesPerRoundNumber) {
    double teamsCount = (1 + Math.sqrt(1 + 8 * totalMatchesPerRoundNumber)) / 2;
    return (int) Math.ceil(teamsCount / 2);
  }
}
