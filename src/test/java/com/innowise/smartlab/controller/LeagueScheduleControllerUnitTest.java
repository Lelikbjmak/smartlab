package com.innowise.smartlab.controller;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.innowise.smartlab.dto.LeagueDto;
import com.innowise.smartlab.dto.MatchDto;
import com.innowise.smartlab.dto.TeamDto;
import com.innowise.smartlab.service.LeagueScheduleService;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WebMvcTest(controllers = LeagueScheduleController.class)
class LeagueScheduleControllerUnitTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private LeagueScheduleService leagueScheduleService;

  @Test
  @Order(1)
  void contextLoads() {
    Assertions.assertNotNull(mockMvc);
    Assertions.assertNotNull(objectMapper);
    Assertions.assertNotNull(leagueScheduleService);
  }

  @Test
  void validComposeSchedule() throws Exception {
    // given
    var league = leagueDtoGetting(List.of(new TeamDto(), new TeamDto()));
    Mockito.when(leagueScheduleService.composeSchedule(league)).thenReturn(emptyList());

    // when
    String jsonResponseBody = mockMvc.perform(post("/api/v1/leagues/schedule")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(league)))
        .andExpect(status().isCreated())
        .andReturn().getResponse().getContentAsString();
    List<MatchDto> scheduledMatches = objectMapper
        .readValue(jsonResponseBody, new TypeReference<>() {
        });

    // then
    org.assertj.core.api.Assertions.assertThat(scheduledMatches).isEmpty();
    Mockito.verify(leagueScheduleService, Mockito.times(1))
        .composeSchedule(league);
  }

  @ParameterizedTest
  @MethodSource("invalidCases")
  void invalidComposeSchedule(LeagueDto league) throws Exception {
    // given
    Mockito.when(leagueScheduleService.composeSchedule(league)).thenReturn(emptyList());

    // when
    mockMvc.perform(post("/api/v1/leagues/schedule")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(league)))
        .andExpect(status().isBadRequest())
        .andReturn().getResponse().getContentAsString();

    // then
    Mockito.verifyNoInteractions(leagueScheduleService);
  }

  private static Stream<LeagueDto> invalidCases() {
    return Stream.of(
        leagueDtoGetting(null),
        leagueDtoGetting(emptyList()),
        leagueDtoGetting(singletonList(new TeamDto())),
        leagueDtoGetting(List.of(new TeamDto(), null))
    );
  }

  private static LeagueDto leagueDtoGetting(List<TeamDto> teams) {
    return new LeagueDto(SPACE, SPACE, teams);
  }
}