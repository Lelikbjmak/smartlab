package com.innowise.smartlab.mapper.impl;

import com.innowise.smartlab.dto.response.LeagueScheduleResponseDto;
import com.innowise.smartlab.entity.LeagueSchedule;
import com.innowise.smartlab.mapper.GenericMapper;
import java.util.List;
import org.apache.commons.lang3.NotImplementedException;
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

  @Override
  public LeagueSchedule toEntity(LeagueScheduleResponseDto dto) {
    throw new NotImplementedException();
  }

  @Override
  public List<LeagueScheduleResponseDto> toDtoList(List<LeagueSchedule> entityList) {
    throw new NotImplementedException();
  }

  @Override
  public List<LeagueSchedule> toEntityList(List<LeagueScheduleResponseDto> dtoList) {
    throw new NotImplementedException();
  }
}
