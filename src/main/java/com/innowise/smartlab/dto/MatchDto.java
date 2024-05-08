package com.innowise.smartlab.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record MatchDto(

    TeamDto homeTeam,

    TeamDto awayTeam,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy hh:mm")
    LocalDateTime dateTime
) {

}
