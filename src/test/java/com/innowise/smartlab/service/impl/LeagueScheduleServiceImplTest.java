package com.innowise.smartlab.service.impl;

import static com.innowise.smartlab.util.LeagueUtils.calculateAwayTeamIndex;
import static com.innowise.smartlab.util.LeagueUtils.calculateHomeTeamIndex;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.innowise.smartlab.config.properties.LeagueScheduleConfigProperties;
import com.innowise.smartlab.entity.League;
import com.innowise.smartlab.entity.LeagueSchedule;
import com.innowise.smartlab.entity.Match;
import com.innowise.smartlab.entity.Team;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LeagueScheduleServiceImplTest {

  @Mock
  private LeagueScheduleConfigProperties properties;

  @InjectMocks
  private LeagueScheduleServiceImpl service;

  private static final Duration DEFAULT_GAME_BREAK_IN_DAYS = Duration.ofDays(1);
  private static final Duration DEFAULT_ROUND_BREAK_IN_DAYS = Duration.ofDays(7);

  private static League leagueGetting(int teamSize) {
    var teams = IntStream.range(0, teamSize)
        .mapToObj(it -> teamGetting("Team" + it))
        .toList();
    return new League("League", "Deutschland", teams);
  }

  private static Team teamGetting(String name) {
    return new Team(name, Year.now());
  }

  private void mockLeagueScheduleConfigPropertiesGetting(LocalDate startDate, LocalTime startTime,
      Duration leagueGameBreakIntervalInDays, Duration leagueRoundBreakIntervalInDays) {
    when(properties.leagueStartDate()).thenReturn(startDate);
    when(properties.leagueGameStartTime()).thenReturn(startTime);
    when(properties.leagueGameBreakIntervalInDays()).thenReturn(leagueGameBreakIntervalInDays);
    when(properties.leagueRoundBreakIntervalInDays()).thenReturn(leagueRoundBreakIntervalInDays);
  }

  private void mockLeagueScheduleConfigPropertiesGetting(LocalDate startDate, LocalTime startTime) {
    when(properties.leagueStartDate()).thenReturn(startDate);
    when(properties.leagueGameStartTime()).thenReturn(startTime);
    when(properties.leagueGameBreakIntervalInDays()).thenReturn((DEFAULT_GAME_BREAK_IN_DAYS));
    when(properties.leagueRoundBreakIntervalInDays()).thenReturn(DEFAULT_ROUND_BREAK_IN_DAYS);
  }

  @Test
  void contextLoads() {
    Assertions.assertNotNull(service);
    Assertions.assertNotNull(properties);
  }

  @Test
  void testScheduleComposition() {
    // given
    final int teamSize = 4;
    League league = leagueGetting(teamSize);
    mockLeagueScheduleConfigPropertiesGetting(LocalDate.now(), LocalTime.of(17, 0));
    when(properties.leagueStartDate()).thenReturn(LocalDate.now());

    // when
    LeagueSchedule schedule = service.composeSchedule(league);

    // then
    assertEquals(teamSize * (teamSize - 1),
        schedule.getRounds().stream().mapToLong(it -> it.getMatches().size()).sum());
    schedule.getRounds().forEach(roundMatches -> {
      Map<String, Set<String>> teamPairs = new HashMap<>();
      roundMatches.getMatches().forEach(match -> {
        teamPairs.computeIfAbsent(match.getHomeTeam().getName(), k -> new HashSet<>())
            .add(match.getAwayTeam().getName());
        teamPairs.computeIfAbsent(match.getAwayTeam().getName(), k -> new HashSet<>())
            .add(match.getHomeTeam().getName());
      });

      teamPairs.forEach((team, opponents) -> assertEquals(teamSize - 1,
          opponents.size()));
    });
    verify(properties, atLeastOnce()).leagueStartDate();
    verify(properties, atLeastOnce()).leagueGameStartTime();
  }

  @Test
  void testScheduleCompositionMatchOrder() {
    // given
    final int teamSize = 6;
    League league = leagueGetting(teamSize);
    mockLeagueScheduleConfigPropertiesGetting(LocalDate.now(), LocalTime.of(17, 0));

    // when
    LeagueSchedule schedule = service.composeSchedule(league);

    // then
    var scheduledMatches = schedule.getRounds().stream()
        .flatMap(it -> it.getMatches().stream())
        .toList();
    assertEquals(teamSize * (teamSize - 1), scheduledMatches.size());
    for (int round = 0; round < teamSize - 1; round++) {
      for (int match = 0; match < teamSize / 2; match++) {
        int matchIndex = round * (teamSize / 2) + match;
        Team expectedHome = league.getTeams().get(calculateHomeTeamIndex(teamSize, round, match));
        Team expectedAway = league.getTeams().get(calculateAwayTeamIndex(teamSize, round, match));

        Match scheduledMatch = scheduledMatches.get(matchIndex);
        assertEquals(expectedHome, scheduledMatch.getHomeTeam(),
            "Round " + round + " Match " + match + ": Home team does not match expected");
        assertEquals(expectedAway, scheduledMatch.getAwayTeam(),
            "Round " + round + " Match " + match + ": Away team does not match expected");
      }
    }
    verify(properties, atLeastOnce()).leagueStartDate();
    verify(properties, atLeastOnce()).leagueGameStartTime();
  }

  @Test
  void testScheduleCompositionMatchConductionDateOrder() {
    // given
    final int teamSize = 6;
    final int matchesPerDay = teamSize / 2;
    League league = leagueGetting(teamSize);
    LocalTime startTime = LocalTime.of(15, 0);
    LocalDate startDate = LocalDate.of(2020, 10, 17);
    mockLeagueScheduleConfigPropertiesGetting(startDate, startTime, Duration.ofDays(7),
        Duration.ofDays(21));

    // when
    LeagueSchedule schedule = service.composeSchedule(league);

    // then
    LocalDate expectedDate = startDate;
    boolean inSecondRound = false;
    var scheduledMatches = schedule.getRounds().stream()
        .flatMap(it -> it.getMatches().stream())
        .toList();
    for (int i = 0; i < scheduledMatches.size(); i++) {
      Match match = scheduledMatches.get(i);

      if (i % matchesPerDay == 0 && i != 0) {
        if (!inSecondRound && i == teamSize * (teamSize - 1) / 2) {
          expectedDate = expectedDate.plusDays(21);
          inSecondRound = true;
        } else {
          expectedDate = expectedDate.plusDays(7);
        }
      }

      LocalDateTime expectedDateTime = LocalDateTime.of(expectedDate, startTime);

      assertEquals(expectedDateTime, match.getConductionDateTime(),
          "Match datetime does not match expected for match index " + i);
    }

    verify(properties, atLeastOnce()).leagueStartDate();
    verify(properties, atLeastOnce()).leagueGameStartTime();
    verify(properties, atLeastOnce()).leagueGameBreakIntervalInDays();
    verify(properties, atLeastOnce()).leagueRoundBreakIntervalInDays();
  }
}