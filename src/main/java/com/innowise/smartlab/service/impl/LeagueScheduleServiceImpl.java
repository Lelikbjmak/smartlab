package com.innowise.smartlab.service.impl;

import static com.innowise.smartlab.util.LeagueUtils.calculateAwayTeamIndex;
import static com.innowise.smartlab.util.LeagueUtils.calculateHomeTeamIndex;
import static com.innowise.smartlab.util.LeagueUtils.transposeMatch;
import static lombok.AccessLevel.PRIVATE;

import com.innowise.smartlab.config.properties.LeagueScheduleConfigProperties;
import com.innowise.smartlab.dto.DummyTeamDto;
import com.innowise.smartlab.dto.LeagueDto;
import com.innowise.smartlab.dto.MatchDto;
import com.innowise.smartlab.dto.TeamDto;
import com.innowise.smartlab.service.LeagueScheduleService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class LeagueScheduleServiceImpl implements LeagueScheduleService {

  static int FIRST_TEAM_INDEX = 0;
  static long WEEK_OFFSET_IN_DAYS = 7L;

  LeagueScheduleConfigProperties leagueScheduleConfigProperties;

  @Override
  public List<MatchDto> composeSchedule(LeagueDto league) {
    List<MatchDto> schedule = new LinkedList<>();
    List<MatchDto> firstRoundMatches = composeFirstRoundMatches(league);
    List<MatchDto> secondRoundMatches = composeSecondRoundMatches(firstRoundMatches);

    schedule.addAll(firstRoundMatches);
    schedule.addAll(secondRoundMatches);
    return schedule;
  }

  private List<MatchDto> composeFirstRoundMatches(LeagueDto league) {
    List<TeamDto> teams = league.teams().stream()
        .filter(Objects::nonNull)
        .toList();
    int teamCount = teams.size();

    List<MatchDto> firstRoundMatches = new ArrayList<>();
    IntStream.range(0, teamCount - 1).forEach(round ->
        IntStream.range(0, teamCount / 2).forEach(match -> {
          int homeTeamIndex = calculateHomeTeamIndex(teamCount, round, match);
          int awayTeamIndex = calculateAwayTeamIndex(teamCount, round, match);
          firstRoundMatches.add(createMatchDto(teams, homeTeamIndex, awayTeamIndex, round));
        })
    );
    return firstRoundMatches;
  }

  private MatchDto createMatchDto(List<TeamDto> teams, int home, int away, int round) {
    LocalDateTime matchDateTime = LocalDateTime.of(
        leagueScheduleConfigProperties.leagueStartDate().plusWeeks(round),
        leagueScheduleConfigProperties.leagueGameStartTime()
    );
    return new MatchDto(teams.get(home), teams.get(away), matchDateTime);
  }

  private List<MatchDto> composeSecondRoundMatches(List<MatchDto> firstRoundMatches) {
    List<MatchDto> secondRoundMatches = new ArrayList<>();
    LocalDateTime secondRoundStart = firstRoundMatches.get(firstRoundMatches.size() - 1).dateTime()
        .plusDays(leagueScheduleConfigProperties.leagueRoundBreakInterval().toDays()
            + WEEK_OFFSET_IN_DAYS);

    for (MatchDto match : firstRoundMatches) {
      secondRoundMatches.add(transposeMatch(match));
      secondRoundStart = secondRoundStart.plusDays(
          leagueScheduleConfigProperties.leagueGameBreakInterval().toDays());
    }

    return secondRoundMatches;
  }

  // TODO: not supported by Bundesliga
  private void compensateDisbalancedTeams(LeagueDto league) {
    boolean isOdd = league.teams().size() % 2 != 0;
    if (isOdd) {
      league.teams().add(new DummyTeamDto());
    }
  }
}
