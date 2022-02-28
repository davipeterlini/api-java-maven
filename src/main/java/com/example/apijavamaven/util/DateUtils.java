package com.example.apijavamaven.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DateUtils {

  public final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
  public final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  public final DateTimeFormatter DATE_TIME_FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
  public static final String BRAZILIAN_DATE_TIME_FORMATTER = "dd/MM/yyyy HH:mm:ss";
  private static final String DD_MM_YYYY = "dd/MM/yyyy";

  /**
   * Convert string to LocalDate. String format expected 2020-12-18
   */
  public LocalDate asLocalDate(String date) {
    return LocalDate.parse(date);
  }

  /**
   * Method responsible to convert localDate to brazilian format {@link DateUtils#DD_MM_YYYY}
   */
  public String asString(LocalDate date) {
    var formatter = DateTimeFormatter.ofPattern(DD_MM_YYYY);
    return date.format(formatter);
  }

  /**
   * Method responsible to convert localDate to brazilian format {@link DateUtils#DD_MM_YYYY}
   */
  public String asString(LocalDateTime dateTime) {
    return asString(dateTime.toLocalDate());
  }

  /**
   * Method responsible to return hour, minute and second from LocalDateTime Object as String
   */
  public String asTime(LocalDateTime dateTime) {
    return dateTime.format(TIME_FORMATTER);
  }

  /**
   * Convert string to LocalDateTime. String format expected 2021-03-08T12:13:14
   */
  public LocalDateTime asLocalDateTime(String localDateTime) {
    return LocalDateTime.parse(localDateTime);
  }

  /**
   * Method responsible to convert localDateTime to brazilian format dd/MM/yyyy HH:mm:ss
   */
  public String asBrazilianLocalDateTime(LocalDateTime date) {
    var formatter = DateTimeFormatter.ofPattern(BRAZILIAN_DATE_TIME_FORMATTER);
    return date.format(formatter);
  }

  /**
   * Method responsible to convert LocalDate into String as "yyyy-dd-MM" format. This method is useful to call legacy endpoints
   * which only accept this format.
   */
  public static String asYeahDayMonth(LocalDate localDate) {
    var yearDayMonthFormat = DateTimeFormatter.ofPattern("yyyy-dd-MM");
    return localDate.format(yearDayMonthFormat);
  }
}
