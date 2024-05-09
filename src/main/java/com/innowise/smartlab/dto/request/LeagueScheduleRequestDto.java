package com.innowise.smartlab.dto.request;

import com.innowise.smartlab.annotation.EvenSize;
import com.innowise.smartlab.dto.TeamDto;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record LeagueScheduleRequestDto(

    String league,

    String country,

    @NotEmpty(message = "Teams are mandatory")
    @EvenSize(message = "Size of teams must be even")
    List<TeamDto> teams
) {

}
