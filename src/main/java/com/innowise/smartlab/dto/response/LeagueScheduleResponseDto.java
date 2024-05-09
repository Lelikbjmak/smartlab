package com.innowise.smartlab.dto.response;

import com.innowise.smartlab.dto.RoundDto;
import java.util.List;

public record LeagueScheduleResponseDto(

    String league,

    String country,

    List<RoundDto> rounds
) {

}
