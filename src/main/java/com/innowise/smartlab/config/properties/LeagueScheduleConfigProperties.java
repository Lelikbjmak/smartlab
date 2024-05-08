package com.innowise.smartlab.config.properties;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;


@Validated
@ConfigurationProperties("app.league-schedule")
public record LeagueScheduleConfigProperties(

    @NonNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate leagueStartDate,

    @NonNull
    LocalTime leagueGameStartTime,

    @NonNull
    Duration leagueGameBreakInterval,

    @NonNull
    Duration leagueRoundBreakInterval
) {

}
