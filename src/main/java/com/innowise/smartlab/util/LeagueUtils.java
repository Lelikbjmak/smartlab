package com.innowise.smartlab.util;

import com.innowise.smartlab.dto.MatchDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class LeagueUtils {

  private static final int FIRST_TEAM_INDEX = 0;

  public static MatchDto transposeMatch(MatchDto match) {
    return new MatchDto(match.awayTeam(), match.homeTeam(), match.dateTime());
  }

  public static int calculateHomeTeamIndex(int teamCount, int round, int match) {
    return (round + match) % (teamCount - 1);
  }

  public static int calculateAwayTeamIndex(int teamCount, int round, int match) {
    return match == FIRST_TEAM_INDEX ? teamCount - 1
        : (teamCount - 1 - match + round) % (teamCount - 1);
  }
}
