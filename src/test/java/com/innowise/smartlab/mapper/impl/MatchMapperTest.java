package com.innowise.smartlab.mapper.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.innowise.smartlab.dto.MatchDto;
import com.innowise.smartlab.dto.TeamDto;
import com.innowise.smartlab.entity.Match;
import com.innowise.smartlab.entity.Team;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MatchMapperTest {

  @Mock
  private TeamMapper teamMapper;

  @InjectMocks
  private MatchMapper matchMapper;

  @Test
  void toDto() {
    // given
    Team homeTeam = new Team("Home Team", Year.now());
    Team awayTeam = new Team("Away Team", Year.now());
    Match match = new Match(homeTeam, awayTeam, LocalDateTime.now());

    TeamDto homeTeamDto = new TeamDto("Home Team", Year.now());
    TeamDto awayTeamDto = new TeamDto("Away Team", Year.now());
    when(teamMapper.toDto(homeTeam)).thenReturn(homeTeamDto);
    when(teamMapper.toDto(awayTeam)).thenReturn(awayTeamDto);

    // when
    MatchDto result = matchMapper.toDto(match);

    // then
    assertEquals(homeTeamDto, result.homeTeam());
    assertEquals(awayTeamDto, result.awayTeam());
    assertEquals(match.getConductionDateTime(), result.conductionDateTime());
  }

  @Test
  void toEntity() {
    // given
    TeamDto homeTeamDto = new TeamDto("Home Team", Year.now());
    TeamDto awayTeamDto = new TeamDto("Away Team", Year.now());
    LocalDateTime dateTime = LocalDateTime.now();
    MatchDto matchDto = new MatchDto(homeTeamDto, awayTeamDto, dateTime);

    Team homeTeam = new Team("Home Team", Year.now());
    Team awayTeam = new Team("Away Team", Year.now());
    when(teamMapper.toEntity(homeTeamDto)).thenReturn(homeTeam);
    when(teamMapper.toEntity(awayTeamDto)).thenReturn(awayTeam);

    // when
    Match result = matchMapper.toEntity(matchDto);

    // then
    assertEquals(homeTeam, result.getHomeTeam());
    assertEquals(awayTeam, result.getAwayTeam());
    assertEquals(dateTime, result.getConductionDateTime());
    verify(teamMapper, times(2)).toEntity(any());
  }

  @Test
  void toDtoList() {
    // given
    Team homeTeam1 = new Team("Home Team 1", Year.now());
    Team awayTeam1 = new Team("Away Team 1", Year.now());
    Team homeTeam2 = new Team("Home Team 2", Year.now());
    Team awayTeam2 = new Team("Away Team 2", Year.now());
    List<Match> entityList = List.of(
        new Match(homeTeam1, awayTeam1, LocalDateTime.now()),
        new Match(homeTeam2, awayTeam2, LocalDateTime.now().plusDays(1))
    );

    when(teamMapper.toDto(any(Team.class))).thenAnswer(invocation -> new TeamDto(
        ((Team) invocation.getArgument(0)).getName(),
        ((Team) invocation.getArgument(0)).getFoundingDate()
    ));

    // when
    List<MatchDto> dtoList = matchMapper.toDtoList(entityList);

    // then
    assertEquals(2, dtoList.size());
    verify(teamMapper, times(4)).toDto(any(Team.class));
  }

  @Test
  void toEntityList() {
    // given
    TeamDto homeTeamDto1 = new TeamDto("Home Team 1", Year.now());
    TeamDto awayTeamDto1 = new TeamDto("Away Team 1", Year.now());
    TeamDto homeTeamDto2 = new TeamDto("Home Team 2", Year.now());
    TeamDto awayTeamDto2 = new TeamDto("Away Team 2", Year.now());
    List<MatchDto> dtoList = List.of(
        new MatchDto(homeTeamDto1, awayTeamDto1, LocalDateTime.now()),
        new MatchDto(homeTeamDto2, awayTeamDto2, LocalDateTime.now().plusDays(1))
    );

    when(teamMapper.toEntity(any(TeamDto.class))).thenAnswer(invocation -> new Team(
        ((TeamDto) invocation.getArgument(0)).name(),
        ((TeamDto) invocation.getArgument(0)).foundingDate()
    ));

    // when
    List<Match> entityList = matchMapper.toEntityList(dtoList);

    // then
    assertEquals(2, entityList.size());
    verify(teamMapper, times(4)).toEntity(any(TeamDto.class));  // Each team mapped once
  }
}