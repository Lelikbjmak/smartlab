package com.innowise.smartlab.controller;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.apache.commons.lang3.StringUtils.SPACE;

import com.innowise.smartlab.dto.TeamDto;
import com.innowise.smartlab.dto.request.LeagueScheduleRequestDto;
import com.innowise.smartlab.dto.response.LeagueScheduleResponseDto;
import com.innowise.smartlab.test.ResourceUtils;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LeagueScheduleControllerIntegrationTest {

  private static final String VALID_SCHEDULE_REQUEST = "schedule/request.json";

  @Autowired
  private TestRestTemplate testRestTemplate;

  private static <T> HttpEntity<T> httpEntityGetting(T payload) {
    return new HttpEntity<>(payload);
  }

  private static Stream<LeagueScheduleRequestDto> invalidCases() {
    return Stream.of(
        leagueScheduleRequestDtoGetting(null),
        leagueScheduleRequestDtoGetting(emptyList()),
        leagueScheduleRequestDtoGetting(singletonList(new TeamDto(null, null)))
    );
  }

  private static LeagueScheduleRequestDto leagueScheduleRequestDtoGetting(List<TeamDto> teams) {
    return new LeagueScheduleRequestDto(SPACE, SPACE, teams);
  }

  @Test
  void contextLoads() {
    Assertions.assertNotNull(testRestTemplate);
  }

  @Test
  void validComposeSchedule() {
    // given
    var payload = ResourceUtils.loadResource(VALID_SCHEDULE_REQUEST,
        LeagueScheduleRequestDto.class);
    var teamCount = payload.teams().size();
    var httpEntity = httpEntityGetting(payload);

    // when
    ResponseEntity<LeagueScheduleResponseDto> response = testRestTemplate
        .exchange("/api/v1/leagues/schedule", HttpMethod.POST, httpEntity,
            LeagueScheduleResponseDto.class);

    // then
    var schedule = response.getBody();
    Assertions.assertNotNull(schedule);
    var matches = schedule.rounds().stream()
        .flatMap(it -> it.matches().stream())
        .toList();
    Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    org.assertj.core.api.Assertions.assertThat(response.getBody().rounds()).isNotEmpty();
    org.assertj.core.api.Assertions.assertThat(matches).isNotEmpty();
    org.assertj.core.api.Assertions.assertThat(matches).hasSize(teamCount * (teamCount - 1));
  }

  @ParameterizedTest
  @MethodSource("invalidCases")
  void invalidComposeSchedule(LeagueScheduleRequestDto payload) {
    // given
    var httpEntity = httpEntityGetting(payload);

    // when
    ResponseEntity<Object> response = testRestTemplate
        .exchange("/api/v1/leagues/schedule", HttpMethod.POST, httpEntity, Object.class);

    // then
    Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }
}