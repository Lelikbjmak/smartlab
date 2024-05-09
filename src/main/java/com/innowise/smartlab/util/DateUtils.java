package com.innowise.smartlab.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Supplier;

public class DateUtils {

  public static String formatDate(Supplier<LocalDateTime> dateSupplier, String pattern) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
    return dateSupplier.get().format(formatter);
  }
}
