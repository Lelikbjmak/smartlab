package com.innowise.smartlab.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class TestApplicationConfig {

  @Bean
  @Primary
  public ObjectMapper objectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    JavaTimeModule javaTimeModule = new JavaTimeModule();

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy hh:mm");
    javaTimeModule.addDeserializer(LocalDateTime.class,
        new LocalDateTimeDeserializer(dateTimeFormatter));

    objectMapper.registerModule(javaTimeModule);
    return objectMapper;
  }
}
