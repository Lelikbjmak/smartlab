package com.innowise.smartlab.mapper.impl;

import com.innowise.smartlab.dto.TeamDto;
import com.innowise.smartlab.entity.Team;
import com.innowise.smartlab.mapper.GenericMapper;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class TeamMapper implements GenericMapper<Team, TeamDto> {

  @Override
  public TeamDto toDto(Team entity) {
    return new TeamDto(entity.getName(), entity.getFoundingDate());
  }

  @Override
  public Team toEntity(TeamDto dto) {
    return new Team(dto.name(), dto.foundingDate());
  }

  @Override
  public List<TeamDto> toDtoList(List<Team> entityList) {
    return entityList.stream()
        .map(this::toDto)
        .toList();
  }

  @Override
  public List<Team> toEntityList(List<TeamDto> dtoList) {
    return dtoList.stream()
        .map(this::toEntity)
        .toList();
  }
}
