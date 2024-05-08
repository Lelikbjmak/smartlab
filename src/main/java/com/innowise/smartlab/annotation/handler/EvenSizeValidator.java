package com.innowise.smartlab.annotation.handler;

import com.innowise.smartlab.annotation.EvenSize;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class EvenSizeValidator implements
    ConstraintValidator<EvenSize, List<?>> {

  @Override
  public boolean isValid(List<?> value, ConstraintValidatorContext context) {
    return value == null || value.stream().
        filter(Objects::nonNull)
        .count() % 2 == 0;
  }
}
