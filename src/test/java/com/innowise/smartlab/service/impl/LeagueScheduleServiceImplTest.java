package com.innowise.smartlab.service.impl;

import static com.innowise.smartlab.util.LeagueUtils.calculateAwayTeamIndex;
import static com.innowise.smartlab.util.LeagueUtils.calculateHomeTeamIndex;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.innowise.smartlab.config.properties.LeagueScheduleConfigProperties;
import com.innowise.smartlab.dto.LeagueDto;
import com.innowise.smartlab.dto.MatchDto;
import com.innowise.smartlab.dto.TeamDto;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Year;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LeagueScheduleServiceImplTest {

  @Mock
  private LeagueScheduleConfigProperties config;

  @InjectMocks
  private LeagueScheduleServiceImpl service;

  @Test
  public void testScheduleComposition() {
    // given
    final int teamSize = 4;
    LeagueDto league = leagueGetting(teamSize);
    when(config.leagueStartDate()).thenReturn(LocalDate.now());
    when(config.leagueGameStartTime()).thenReturn(LocalTime.of(15, 0));

    // when
    List<MatchDto> schedule = service.composeSchedule(league);

    // then
    assertEquals(teamSize * (teamSize - 1),
        schedule.size());
    verify(config, atLeastOnce()).leagueStartDate();
    verify(config, atLeastOnce()).leagueGameStartTime();
  }

  @Test
  public void testScheduleCompositionOrder() {
    // given
    final int teamSize = 6;
    LeagueDto league = leagueGetting(teamSize);
    when(config.leagueStartDate()).thenReturn(LocalDate.now());
    when(config.leagueGameStartTime()).thenReturn(LocalTime.of(15, 0));
    when(config.leagueGameStartTime()).thenReturn(LocalTime.of(15, 0));

    // when
    List<MatchDto> schedule = service.composeSchedule(league);

    // then
    assertEquals(teamSize * (teamSize - 1), schedule.size());
    for (int round = 0; round < teamSize - 1; round++) {
      for (int match = 0; match < teamSize / 2; match++) {
        int matchIndex = round * (teamSize / 2) + match;
        TeamDto expectedHome = league.teams().get(calculateHomeTeamIndex(teamSize, round, match));
        TeamDto expectedAway = league.teams().get(calculateAwayTeamIndex(teamSize, round, match));

        MatchDto scheduledMatch = schedule.get(matchIndex);
        assertEquals(expectedHome, scheduledMatch.homeTeam(),
            "Round " + round + " Match " + match + ": Home team does not match expected");
        assertEquals(expectedAway, scheduledMatch.awayTeam(),
            "Round " + round + " Match " + match + ": Away team does not match expected");
      }
    }
    verify(config, atLeastOnce()).leagueStartDate();
    verify(config, atLeastOnce()).leagueGameStartTime();
  }

  @Test
  public void testScheduleCompositionMatchDateOrder() {
    // given
    final int teamSize = 6;
    LeagueDto league = leagueGetting(teamSize);
    when(config.leagueStartDate()).thenReturn(LocalDate.now());
    when(config.leagueGameStartTime()).thenReturn(LocalTime.of(15, 0));
    when(config.leagueGameStartTime()).thenReturn(LocalTime.of(15, 0));

    // when
    List<MatchDto> schedule = service.composeSchedule(league);

    // then
    assertEquals(teamSize * (teamSize - 1), schedule.size());
    for (int round = 0; round < teamSize - 1; round++) {
      for (int match = 0; match < teamSize / 2; match++) {
        int matchIndex = round * (teamSize / 2) + match;
        TeamDto expectedHome = league.teams().get(calculateHomeTeamIndex(teamSize, round, match));
        TeamDto expectedAway = league.teams().get(calculateAwayTeamIndex(teamSize, round, match));

        MatchDto scheduledMatch = schedule.get(matchIndex);
        assertEquals(expectedHome, scheduledMatch.homeTeam(),
            "Round " + round + " Match " + match + ": Home team does not match expected");
        assertEquals(expectedAway, scheduledMatch.awayTeam(),
            "Round " + round + " Match " + match + ": Away team does not match expected");
      }
    }
    verify(config, atLeastOnce()).leagueStartDate();
    verify(config, atLeastOnce()).leagueGameStartTime();
  }

  private static LeagueDto leagueGetting(int teamSize) {
    var teams = IntStream.range(0, teamSize)
        .mapToObj(it -> teamGetting("Team" + it))
        .toList();
    return new LeagueDto("League", "Deutschland", teams);
  }

  private static TeamDto teamGetting(String name) {
    return TeamDto.builder()
        .name(name)
        .foundingDate(Year.now())
        .build();
  }
}