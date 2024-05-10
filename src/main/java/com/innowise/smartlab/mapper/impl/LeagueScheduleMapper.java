package com.innowise.smartlab.mapper.impl;

import com.innowise.smartlab.dto.response.LeagueScheduleResponseDto;
import com.innowise.smartlab.entity.LeagueSchedule;
import com.innowise.smartlab.mapper.GenericMapper;
import org.springframework.stereotype.Component;

@Component
public class LeagueScheduleMapper implements
    GenericMapper<LeagueSchedule, LeagueScheduleResponseDto> {

  private final RoundMapper roundMapper;

  public LeagueScheduleMapper(RoundMapper roundMapper) {
    this.roundMapper = roundMapper;
  }

  @Override
  public LeagueScheduleResponseDto toDto(LeagueSchedule entity) {
    var rounds = roundMapper.toDtoList(entity.getRounds());
    return new LeagueScheduleResponseDto(entity.getLeague(), entity.getCountry(), rounds);
  }
}
