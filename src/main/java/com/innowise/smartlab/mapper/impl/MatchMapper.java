package com.innowise.smartlab.mapper.impl;

import com.innowise.smartlab.dto.MatchDto;
import com.innowise.smartlab.entity.Match;
import com.innowise.smartlab.mapper.GenericMapper;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class MatchMapper implements GenericMapper<Match, MatchDto> {

  private final TeamMapper teamMapper;

  public MatchMapper(TeamMapper teamMapper) {
    this.teamMapper = teamMapper;
  }

  @Override
  public MatchDto toDto(Match entity) {
    var homeTeam = teamMapper.toDto(entity.getHomeTeam());
    var awayTeam = teamMapper.toDto(entity.getAwayTeam());

    return new MatchDto(homeTeam, awayTeam, entity.getConductionDateTime());
  }

  @Override
  public Match toEntity(MatchDto dto) {
    var homeTeam = teamMapper.toEntity(dto.homeTeam());
    var awayTeam = teamMapper.toEntity(dto.awayTeam());

    return new Match(homeTeam, awayTeam, dto.conductionDateTime());
  }

  @Override
  public List<MatchDto> toDtoList(List<Match> entityList) {
    return entityList.stream()
        .map(this::toDto)
        .toList();
  }

  @Override
  public List<Match> toEntityList(List<MatchDto> dtoList) {
    return dtoList.stream()
        .map(this::toEntity)
        .toList();
  }
}
