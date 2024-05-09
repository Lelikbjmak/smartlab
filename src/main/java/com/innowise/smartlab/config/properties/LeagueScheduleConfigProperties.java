package com.innowise.smartlab.config.properties;

import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;


@Validated
@ConfigurationProperties("app.league-schedule")
public record LeagueScheduleConfigProperties(

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate leagueStartDate,

    @NotNull
    LocalTime leagueGameStartTime,

    @NotNull
    Duration leagueGameBreakIntervalInDays,

    @NotNull
    Duration leagueRoundBreakIntervalInDays
) {

}
