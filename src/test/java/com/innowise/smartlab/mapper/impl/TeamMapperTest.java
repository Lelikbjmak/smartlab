package com.innowise.smartlab.mapper.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.innowise.smartlab.dto.TeamDto;
import com.innowise.smartlab.entity.Team;
import com.innowise.smartlab.mapper.GenericMapper;
import java.time.Year;
import java.util.List;
import org.junit.jupiter.api.Test;

class TeamMapperTest {

  private final GenericMapper<Team, TeamDto> teamMapper = new TeamMapper();

  @Test
  void toDto() {
    // given
    Team entity = new Team("FC Barcelona", Year.of(1899));

    // when
    TeamDto dto = teamMapper.toDto(entity);

    // then
    assertEquals("FC Barcelona", dto.name());
    assertEquals(Year.of(1899), dto.foundingDate());
  }

  @Test
  void toEntity() {
    // given
    TeamDto dto = new TeamDto("Real Madrid", Year.of(1902));

    // when
    Team entity = teamMapper.toEntity(dto);

    // then
    assertEquals("Real Madrid", entity.getName());
    assertEquals(Year.of(1902), entity.getFoundingDate());
  }

  @Test
  void toDtoList() {
    // given
    List<Team> entityList = List.of(
        new Team("Liverpool", Year.of(1892)),
        new Team("Manchester United", Year.of(1878))
    );

    // when
    List<TeamDto> dtoList = teamMapper.toDtoList(entityList);

    // then
    assertNotNull(dtoList);
    assertEquals(2, dtoList.size());
    assertEquals("Liverpool", dtoList.get(0).name());
    assertEquals("Manchester United", dtoList.get(1).name());
  }

  @Test
  void toEntityList() {
    // given
    List<TeamDto> dtoList = List.of(
        new TeamDto("Chelsea", Year.of(1905)),
        new TeamDto("Arsenal", Year.of(1886))
    );

    // when
    List<Team> entityList = teamMapper.toEntityList(dtoList);

    // then
    assertNotNull(entityList);
    assertEquals(2, entityList.size());
    assertEquals("Chelsea", entityList.get(0).getName());
    assertEquals("Arsenal", entityList.get(1).getName());
  }

  @Test
  public void testToDtoListWithEmptyList() {
    // given
    List<Team> entityList = List.of();

    // when
    List<TeamDto> dtoList = teamMapper.toDtoList(entityList);

    // then
    assertTrue(dtoList.isEmpty());
  }

  @Test
  public void testToEntityListWithEmptyList() {
    // given
    List<TeamDto> dtoList = List.of();

    // when
    List<Team> entityList = teamMapper.toEntityList(dtoList);

    //then
    assertTrue(entityList.isEmpty());
  }
}