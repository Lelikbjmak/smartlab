package com.innowise.smartlab.dto;

import com.innowise.smartlab.annotation.EvenSize;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record LeagueDto(

    String league,

    String country,

    @NotEmpty(message = "Teams are mandatory")
    @EvenSize(message = "Size of teams must be even")
    List<TeamDto> teams
) {

}
