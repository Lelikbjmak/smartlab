package com.innowise.smartlab.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.time.Year;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
// TODO: replace on record if DummyTeam is excess
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TeamDto {

  String name;

  Year foundingDate;
}
