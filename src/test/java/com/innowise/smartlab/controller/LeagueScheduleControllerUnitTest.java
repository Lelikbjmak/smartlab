package com.innowise.smartlab.controller;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innowise.smartlab.dto.TeamDto;
import com.innowise.smartlab.dto.request.LeagueScheduleRequestDto;
import com.innowise.smartlab.dto.response.LeagueScheduleResponseDto;
import com.innowise.smartlab.entity.League;
import com.innowise.smartlab.entity.Team;
import com.innowise.smartlab.mapper.impl.LeagueMapper;
import com.innowise.smartlab.mapper.impl.LeagueScheduleMapper;
import com.innowise.smartlab.service.LeagueScheduleService;
import java.time.Year;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = LeagueScheduleController.class)
class LeagueScheduleControllerUnitTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private LeagueScheduleService leagueScheduleService;

  @MockBean
  private LeagueScheduleMapper leagueScheduleMapper;
  @MockBean
  private LeagueMapper leagueMapper;

  private static final String COUNTRY = "Deutschland";
  private static final String LEAGUE = "League";
  private static final String TEAM_NAME_PREFIX = "Team";

  private static Stream<LeagueScheduleRequestDto> invalidCases() {
    return Stream.of(
        leagueDtoGetting(null),
        leagueDtoGetting(emptyList()),
        leagueDtoGetting(singletonList(new TeamDto(TEAM_NAME_PREFIX, Year.now())))
    );
  }

  private static LeagueScheduleRequestDto leagueDtoGetting(List<TeamDto> teams) {
    return new LeagueScheduleRequestDto(LEAGUE, COUNTRY, teams);
  }

  private static TeamDto teamDtoGetting(String name) {
    return new TeamDto(name, Year.now());
  }

  private static Team teamGetting(String name) {
    return new Team(name, Year.now());
  }

  private static List<TeamDto> teamDtoListGetting(int size) {
    return IntStream.range(0, size)
        .mapToObj(it -> teamDtoGetting(TEAM_NAME_PREFIX + it))
        .toList();
  }

  private static List<Team> teamListGetting(int size) {
    return IntStream.range(0, size)
        .mapToObj(it -> teamGetting(TEAM_NAME_PREFIX + it))
        .toList();
  }

  @Test
  void contextLoads() {
    Assertions.assertNotNull(mockMvc);
    Assertions.assertNotNull(objectMapper);
    Assertions.assertNotNull(leagueMapper);
    Assertions.assertNotNull(leagueScheduleMapper);
    Assertions.assertNotNull(leagueScheduleService);
  }

  @Test
  void validComposeSchedule() throws Exception {
    // given
    // TODO: refactor - ??? doubt in creating - separately create object or via method (cause we encapsulate our constants usage in method)
    var requestDto = leagueDtoGetting(teamDtoListGetting(2));
    var requestEntity = new League(LEAGUE, COUNTRY, teamListGetting(2));
    var responseDto = new LeagueScheduleResponseDto(LEAGUE, COUNTRY, emptyList());

    Mockito.when(leagueMapper.toEntity(any())).thenReturn(requestEntity);
    Mockito.when(leagueScheduleMapper.toDto(any())).thenReturn(responseDto);
    Mockito.when(leagueScheduleService.composeSchedule(requestEntity)).thenReturn(any());

    // when
    String jsonResponseBody = mockMvc.perform(post("/api/v1/leagues/schedule")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(requestDto)))
        .andExpect(status().isCreated())
        .andReturn().getResponse().getContentAsString();
    LeagueScheduleResponseDto scheduleDto = objectMapper
        .readValue(jsonResponseBody, LeagueScheduleResponseDto.class);

    // then
    org.assertj.core.api.Assertions.assertThat(scheduleDto.rounds()).isEmpty();
    Mockito.verify(leagueScheduleService, Mockito.times(1))
        .composeSchedule(requestEntity);
  }

  @ParameterizedTest
  @MethodSource("invalidCases")
  void invalidComposeSchedule(LeagueScheduleRequestDto leagueDto) throws Exception {
    // when
    mockMvc.perform(post("/api/v1/leagues/schedule")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(leagueDto)))
        .andExpect(status().isBadRequest())
        .andReturn().getResponse().getContentAsString();

    // then
    Mockito.verifyNoInteractions(leagueScheduleService);
  }
}