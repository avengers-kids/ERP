package com.erp.erp.infrastructure.utility;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

public class DateTimeFormatterUtil {

  /**
   * Returns “1st”, “2nd”, “3rd”, “4th”, … “11th”, “12th”, … correctly handling 11–13.
   */
  private static String dayWithSuffix(int day) {
    if (day >= 11 && day <= 13) {
      return day + "th";
    }
    return switch (day % 10) {
      case 1 -> day + "st";
      case 2 -> day + "nd";
      case 3 -> day + "rd";
      default -> day + "th";
    };
  }

  /**
   * Formats a LocalDateTime as: {ordinal-day} {Full-Month-Name}, {year} at {h:mm am/pm}
   * <p>
   * Examples: 2025-01-07T12:00  → “7th January, 2025 at 12:00 pm” 2029-05-14T00:00  → “14th May, 2029 at 12:00 am”
   */
  public static String toReadable(LocalDateTime dt) {
    String day = dayWithSuffix(dt.getDayOfMonth());
    String month = dt.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
    int year = dt.getYear();

    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH);
    String timePart = dt.format(fmt).toLowerCase();

    return String.format("%s %s, %d at %s", day, month, year, timePart);
  }
}
