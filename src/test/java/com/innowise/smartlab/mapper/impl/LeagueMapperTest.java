package com.innowise.smartlab.mapper.impl;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.innowise.smartlab.dto.TeamDto;
import com.innowise.smartlab.dto.request.LeagueScheduleRequestDto;
import com.innowise.smartlab.entity.League;
import java.time.Year;
import java.util.List;
import java.util.stream.IntStream;
import org.apache.commons.lang3.NotImplementedException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LeagueMapperTest {

  @Mock
  private TeamMapper teamMapper;

  @InjectMocks
  private LeagueMapper leagueMapper;

  private static final String LEAGUE = "League";
  private static final String COUNTRY = "Germany";
  private static final String TEAM_PREFIX = "Team";

  private static LeagueScheduleRequestDto leagueScheduleRequestDtoGetting(int teamSize) {
    var teams = IntStream.range(0, teamSize).mapToObj(it -> teamDtoGetting(TEAM_PREFIX + it))
        .toList();
    return new LeagueScheduleRequestDto(LEAGUE, COUNTRY, teams);
  }

  private static TeamDto teamDtoGetting(String name) {
    return new TeamDto(name, Year.now());
  }

  @Test
  void toDto() {
    // given
    League league = new League();

    Assertions.assertThrows(NotImplementedException.class, () -> {
      // when
      leagueMapper.toDto(league);
    });

    // then
    Mockito.verifyNoInteractions(teamMapper);
  }

  @Test
  public void toEntity() {
    // given
    LeagueScheduleRequestDto dto = leagueScheduleRequestDtoGetting(4);

    Mockito.when(teamMapper.toEntityList(dto.teams())).thenReturn(emptyList());

    // when
    League entity = leagueMapper.toEntity(dto);

    // then
    assertEquals(LEAGUE, entity.getLeague());
    assertEquals(COUNTRY, entity.getCountry());
    assertEquals(emptyList(), entity.getTeams());
    Mockito.verify(teamMapper).toEntityList(dto.teams());
  }

  @Test
  void toDtoList() {
    // given
    List<League> entityList = emptyList();

    Assertions.assertThrows(NotImplementedException.class, () -> {
      // when
      leagueMapper.toDtoList(entityList);
    });

    // then
    Mockito.verifyNoInteractions(teamMapper);
  }

  @Test
  void toEntityList() {
    // given
    List<LeagueScheduleRequestDto> dtoList = emptyList();

    Assertions.assertThrows(NotImplementedException.class, () -> {
      // when
      leagueMapper.toEntityList(dtoList);
    });

    // then
    Mockito.verifyNoInteractions(teamMapper);
  }

}