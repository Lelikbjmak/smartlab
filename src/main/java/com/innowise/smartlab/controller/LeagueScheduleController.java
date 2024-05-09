package com.innowise.smartlab.controller;

import static org.springframework.http.HttpStatus.CREATED;

import com.innowise.smartlab.dto.request.LeagueScheduleRequestDto;
import com.innowise.smartlab.dto.response.LeagueScheduleResponseDto;
import com.innowise.smartlab.mapper.impl.LeagueMapper;
import com.innowise.smartlab.mapper.impl.LeagueScheduleMapper;
import com.innowise.smartlab.service.LeagueScheduleService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/leagues")
public class LeagueScheduleController {

  private final LeagueScheduleService leagueScheduleService;

  private final LeagueScheduleMapper leagueScheduleMapper;
  private final LeagueMapper leagueMapper;

  public LeagueScheduleController(LeagueScheduleService leagueScheduleService,
      LeagueScheduleMapper leagueScheduleMapper, LeagueMapper leagueMapper) {
    this.leagueScheduleService = leagueScheduleService;
    this.leagueScheduleMapper = leagueScheduleMapper;
    this.leagueMapper = leagueMapper;
  }

  @ResponseStatus(CREATED)
  @PostMapping("/schedule")
  public LeagueScheduleResponseDto composeLeagueSchedule(@Valid @RequestBody LeagueScheduleRequestDto leagueDto) {
    var league = leagueMapper.toEntity(leagueDto);
    var scheduledLeague = leagueScheduleService.composeSchedule(league);
    return leagueScheduleMapper.toDto(scheduledLeague);
  }
}
