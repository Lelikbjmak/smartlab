package com.innowise.smartlab.dto;

import java.util.List;

public record RoundDto(

    int roundNumber,

    List<MatchDto> matches
) {

}
