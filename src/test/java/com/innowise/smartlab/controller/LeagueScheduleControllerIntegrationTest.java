package com.innowise.smartlab.controller;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.apache.commons.lang3.StringUtils.SPACE;

import com.innowise.smartlab.config.TestApplicationConfig;
import com.innowise.smartlab.dto.LeagueDto;
import com.innowise.smartlab.dto.MatchDto;
import com.innowise.smartlab.dto.TeamDto;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

//@Import(TestApplicationConfig.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LeagueScheduleControllerIntegrationTest {

  @Autowired
  private TestRestTemplate testRestTemplate;

  @Test
  @Order(1)
  void contextLoads() {
    Assertions.assertNotNull(testRestTemplate);
  }

  @Test
  void validComposeSchedule() {
    // given
    var payload = leagueDtoGetting(List.of(new TeamDto(), new TeamDto()));
    var httpEntity = httpEntityGetting(payload);

    // when
    ResponseEntity<List<MatchDto>> response = testRestTemplate
        .exchange("/api/v1/leagues/schedule", HttpMethod.POST, httpEntity,
            new ParameterizedTypeReference<>() {
            });

    // then
    Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    Assertions.assertNotNull(response.getBody());
    org.assertj.core.api.Assertions.assertThat(response.getBody()).isNotEmpty();
    org.assertj.core.api.Assertions.assertThat(response.getBody()).hasSize(2);
  }

  @ParameterizedTest
  @MethodSource("invalidCases")
  void invalidComposeSchedule(LeagueDto payload) {
    // given
    var httpEntity = httpEntityGetting(payload);

    // when
    ResponseEntity<Object> response = testRestTemplate
        .exchange("/api/v1/leagues/schedule", HttpMethod.POST, httpEntity, Object.class);

    // then
    Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  private static <T> HttpEntity<T> httpEntityGetting(T payload) {
    return new HttpEntity<>(payload);
  }

  private static Stream<LeagueDto> invalidCases() {
    return Stream.of(
        leagueDtoGetting(null),
        leagueDtoGetting(emptyList()),
        leagueDtoGetting(singletonList(new TeamDto()))
    );
  }

  private static LeagueDto leagueDtoGetting(List<TeamDto> teams) {
    return new LeagueDto(SPACE, SPACE, teams);
  }
}