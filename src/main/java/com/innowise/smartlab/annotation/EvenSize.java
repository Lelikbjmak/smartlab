package com.innowise.smartlab.annotation;

import com.innowise.smartlab.annotation.handler.EvenSizeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EvenSizeValidator.class)
public @interface EvenSize {

  String message() default "Size must be even";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
