package com.innowise.smartlab.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import org.apache.commons.io.IOUtils;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

public class ResourceUtils {

  private static final ObjectMapper JSON_MAPPER;

  static {
    JSON_MAPPER = new Jackson2ObjectMapperBuilder().build();
  }

  public static <T> T loadResource(String path, Class<T> clazz) {
    try {
      return JSON_MAPPER.readValue(loadString(path), clazz);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  public static String loadString(String path) {
    try {
      return IOUtils.toString(
          Objects.requireNonNull(ResourceUtils.class.getClassLoader().getResourceAsStream(path)),
          StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
