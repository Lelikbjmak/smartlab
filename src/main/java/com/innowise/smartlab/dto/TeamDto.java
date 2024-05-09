package com.innowise.smartlab.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.time.Year;


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record TeamDto(

    String name,

    Year foundingDate
) {

}
