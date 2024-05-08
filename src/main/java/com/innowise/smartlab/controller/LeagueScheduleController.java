package com.innowise.smartlab.controller;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpStatus.CREATED;

import com.innowise.smartlab.dto.LeagueDto;
import com.innowise.smartlab.dto.MatchDto;
import com.innowise.smartlab.service.LeagueScheduleService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/leagues")
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class LeagueScheduleController {

  LeagueScheduleService leagueScheduleService;

  @ResponseStatus(CREATED)
  @PostMapping("/schedule")
  public List<MatchDto> composeLeagueSchedule(@Valid @RequestBody LeagueDto league) {
    return leagueScheduleService.composeSchedule(league);
  }
}
