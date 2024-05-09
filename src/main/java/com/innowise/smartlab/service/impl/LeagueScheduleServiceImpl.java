package com.innowise.smartlab.service.impl;

import static com.innowise.smartlab.util.DateUtils.formatDate;
import static com.innowise.smartlab.util.LeagueUtils.calculateAwayTeamIndex;
import static com.innowise.smartlab.util.LeagueUtils.calculateHomeTeamIndex;
import static com.innowise.smartlab.util.LeagueUtils.computeTeamPairsNumber;
import static com.innowise.smartlab.util.LeagueUtils.transposeMatch;

import com.innowise.smartlab.config.properties.LeagueScheduleConfigProperties;
import com.innowise.smartlab.entity.League;
import com.innowise.smartlab.entity.LeagueSchedule;
import com.innowise.smartlab.entity.Match;
import com.innowise.smartlab.entity.Round;
import com.innowise.smartlab.entity.Team;
import com.innowise.smartlab.service.LeagueScheduleService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LeagueScheduleServiceImpl implements LeagueScheduleService {

  private static final Integer INITIAL_ROUND_NUMBER = 1;

  private final LeagueScheduleConfigProperties leagueScheduleConfigProperties;

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  public LeagueScheduleServiceImpl(LeagueScheduleConfigProperties leagueScheduleConfigProperties) {
    this.leagueScheduleConfigProperties = leagueScheduleConfigProperties;
  }

  @Override
  public LeagueSchedule composeSchedule(League league) {
    Round firstRound = composeFirstRoundMatches(league);
    Round secondRound = composeSecondRoundMatches(firstRound);
    List<Round> leagueRounds = Arrays.asList(firstRound, secondRound);

    leagueRounds.stream()
        .flatMap(it -> it.getMatches().stream())
        .forEach(it -> logger.info("{}   {}  {}",
            formatDate(it::getConductionDateTime, "dd.MM.yyyy hh:mm"),
            it.getHomeTeam().getName(), it.getAwayTeam().getName()));

    return new LeagueSchedule(league.getLeague(), league.getCountry(), leagueRounds);
  }

  private Round composeFirstRoundMatches(League league) {
    var firstRound = new Round();
    firstRound.setRoundNumber(INITIAL_ROUND_NUMBER);

    List<Team> teams = league.getTeams().stream()
        .filter(Objects::nonNull)
        .toList();
    int teamCount = teams.size();

    List<Match> firstRoundMatches = new ArrayList<>();
    for (int round = 0; round < teamCount - 1; round++) {
      for (int match = 0; match < teamCount / 2; match++) {
        int homeTeamIndex = calculateHomeTeamIndex(teamCount, round, match);
        int awayTeamIndex = calculateAwayTeamIndex(teamCount, round, match);
        Match newMatch = createMatch(teams, homeTeamIndex, awayTeamIndex, round);
        firstRoundMatches.add(newMatch);
      }
    }
    firstRound.setMatches(firstRoundMatches);
    return firstRound;
  }

  private Match createMatch(List<Team> teams, int home, int away, int round) {
    LocalDateTime matchDateTime = LocalDateTime.of(
        leagueScheduleConfigProperties.leagueStartDate().plusDays(
            round * leagueScheduleConfigProperties.leagueGameBreakIntervalInDays().toDays()),
        leagueScheduleConfigProperties.leagueGameStartTime());
    return new Match(teams.get(home), teams.get(away), matchDateTime);
  }

  private Round composeSecondRoundMatches(Round previousRound) {
    Round newRound = new Round();
    newRound.setRoundNumber(previousRound.getRoundNumber() + 1);

    var previousRoundMatches = previousRound.getMatches();
    List<Match> newRoundMatches = new ArrayList<>();
    LocalDateTime secondRoundStartDate = previousRoundMatches.get(previousRoundMatches.size() - 1)
        .getConductionDateTime()
        .plusDays(leagueScheduleConfigProperties.leagueRoundBreakIntervalInDays().toDays());

    for (int i = 0, j = 1; i < previousRoundMatches.size(); i++, j++) {
      var transposedMatch = transposeMatch(previousRoundMatches.get(i));
      transposedMatch.setConductionDateTime(secondRoundStartDate);
      newRoundMatches.add(transposedMatch);
      if (j == computeTeamPairsNumber(previousRoundMatches.size())) {
        secondRoundStartDate = secondRoundStartDate.plusDays(
            leagueScheduleConfigProperties.leagueGameBreakIntervalInDays().toDays());
        j = 0;
      }
    }
    newRound.setMatches(newRoundMatches);

    return newRound;
  }
}
