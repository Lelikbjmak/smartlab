package com.innowise.smartlab;

import com.innowise.smartlab.config.properties.LeagueScheduleConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(LeagueScheduleConfigProperties.class)
public class SmartlabApplication {

  public static void main(String[] args) {
    SpringApplication.run(SmartlabApplication.class, args);
  }

}
