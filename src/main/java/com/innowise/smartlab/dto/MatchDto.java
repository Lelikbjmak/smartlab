package com.innowise.smartlab.dto;

import java.time.LocalDateTime;

public record MatchDto(

    TeamDto homeTeam,

    TeamDto awayTeam,

//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy hh:mm")
    // TODO: does current format is mandatory in json response??
    LocalDateTime conductionDateTime
) {

}
