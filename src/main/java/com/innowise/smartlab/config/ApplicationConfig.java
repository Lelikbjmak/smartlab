package com.innowise.smartlab.config;

import com.innowise.smartlab.config.properties.LeagueScheduleConfigProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(LeagueScheduleConfigProperties.class)
public class ApplicationConfig {

}
