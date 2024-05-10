package com.innowise.smartlab.mapper.impl;

import com.innowise.smartlab.dto.request.LeagueScheduleRequestDto;
import com.innowise.smartlab.entity.League;
import com.innowise.smartlab.mapper.GenericMapper;
import org.springframework.stereotype.Component;

@Component
public class LeagueMapper implements GenericMapper<League, LeagueScheduleRequestDto> {

  private final TeamMapper teamMapper;

  public LeagueMapper(TeamMapper teamMapper) {
    this.teamMapper = teamMapper;
  }

  @Override
  public League toEntity(LeagueScheduleRequestDto dto) {
    return new League(dto.league(), dto.country(), teamMapper.toEntityList(dto.teams()));
  }
}
