package com.innowise.smartlab.mapper.impl;

import com.innowise.smartlab.dto.RoundDto;
import com.innowise.smartlab.entity.Round;
import com.innowise.smartlab.mapper.GenericMapper;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class RoundMapper implements GenericMapper<Round, RoundDto> {

  private final MatchMapper matchMapper;

  public RoundMapper(MatchMapper matchMapper) {
    this.matchMapper = matchMapper;
  }

  @Override
  public RoundDto toDto(Round entity) {
    return new RoundDto(entity.getRoundNumber(), matchMapper.toDtoList(entity.getMatches()));
  }

  @Override
  public Round toEntity(RoundDto dto) {
    return new Round(dto.roundNumber(), matchMapper.toEntityList(dto.matches()));
  }

  @Override
  public List<RoundDto> toDtoList(List<Round> entityList) {
    return entityList.stream()
        .map(this::toDto)
        .toList();
  }

  @Override
  public List<Round> toEntityList(List<RoundDto> dtoList) {
    return dtoList.stream()
        .map(this::toEntity)
        .toList();
  }
}
