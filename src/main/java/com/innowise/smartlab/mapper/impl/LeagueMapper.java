package com.innowise.smartlab.mapper.impl;

import com.innowise.smartlab.dto.request.LeagueScheduleRequestDto;
import com.innowise.smartlab.entity.League;
import com.innowise.smartlab.mapper.GenericMapper;
import java.util.List;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Component;

@Component
public class LeagueMapper implements GenericMapper<League, LeagueScheduleRequestDto> {

  private final TeamMapper teamMapper;

  public LeagueMapper(TeamMapper teamMapper) {
    this.teamMapper = teamMapper;
  }

  @Override
  public LeagueScheduleRequestDto toDto(League entity) {
    throw new NotImplementedException("Method not implemented");
  }

  @Override
  public League toEntity(LeagueScheduleRequestDto dto) {
    return new League(dto.league(), dto.country(), teamMapper.toEntityList(dto.teams()));
  }

  @Override
  public List<LeagueScheduleRequestDto> toDtoList(List<League> entityList) {
    throw new NotImplementedException("Method not implemented");
  }

  @Override
  public List<League> toEntityList(List<LeagueScheduleRequestDto> dtoList) {
    throw new NotImplementedException("Method not implemented");
  }
}
