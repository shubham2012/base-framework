package com.base.commons.utils;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/** Common Utilities to be used across the application */
public class DateTimeUtils {

    /**
     * Date time formatter to be used when formatting java.time.* classes. Formatter from the main spring boot
     * properties do not apply for java.time classes
     *
     * @return
     */
    public static DateTimeFormatter getDateTimeFormatter() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * Get date formatter for specified pattern
     *
     * @param format
     * @return
     */
    public static DateTimeFormatter getDateTimeFormatter(String format) {
        return DateTimeFormatter.ofPattern(format);
    }
    /**
     * Get dateTime (java.time package) object from date String
     *
     * @param dateString
     * @return
     */
    public static LocalDateTime stringToDate(String dateString) {
        return LocalDateTime.parse(dateString, getDateTimeFormatter());
    }

    /**
     * Get date object from a string matching the pattern specified
     *
     * @param dateString
     * @param format
     * @return
     */
    public static LocalDateTime stringToDate(String dateString, String format) {
        return LocalDateTime.parse(dateString, getDateTimeFormatter(format));
    }

    /**
     * Get LocalTime object from a string matching the pattern specified
     *
     * @param timeString
     * @param format
     * @return
     */
    public static LocalTime stringToTime(String timeString, String format) {
        return LocalTime.parse(timeString, getDateTimeFormatter(format));
    }

    /**
     * Get the start of the week.
     *
     * @return
     */
    public static LocalDateTime getStartOfWeek() {
        return LocalDateTime.now().with(DayOfWeek.MONDAY).withHour(0).withMinute(0).withSecond(0).withNano(0);
    }

    public static LocalDateTime getStartOfTheDay() {
        return LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
    }

    /**
     * Gets current date based on Jakarta timezone
     *
     * @return
     */
    public static LocalDate getCurrentDateInJKT() {
        return LocalDate.now(Clock.system(ZoneId.of("Asia/Jakarta")));
    }

    /**
     * Gets current date time based on Jakarta timezone
     *
     * @return
     */
    public static LocalDateTime getCurrentDateTimeInJKT() {
        return LocalDateTime.now(Clock.system(ZoneId.of("Asia/Jakarta")));
    }

    /**
     * Get the end of the current day until the last second Ex. If current date is "2022-07-26", it will return
     * 2022-07-26 23:59:59
     *
     * @param
     * @return LocalDateTime
     */
    public static LocalDateTime getEndOfTheDay() {
        return LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
    }

    /**
     * Get the end of the day until the last second for a given date Ex. For "2022-08-22", it will return "2022-08-22
     * 23:59:59"
     *
     * @param date
     * @return LocalDateTime
     */
    public static LocalDateTime getEndOfTheDay(LocalDate date) {
        return date.atTime(23, 59, 59);
    }

    /**
     * Get local date object from a string matching the pattern specified Converts a date string into LocalDate
     *
     * @param dateString
     * @param format
     * @return LocalDate
     */
    public static LocalDate stringToLocalDate(String dateString, String format) {
        return LocalDate.parse(dateString, getDateTimeFormatter(format));
    }

    /**
     * Convert JKT time to UTC for a date. Ex. "2022-08-20 12:00:00" JKT will return "2022-08-20 5:00:00" UTC
     *
     * @param dateInJkt
     * @return LocalDateTime
     */
    public static LocalDateTime convertJktToUtc(LocalDateTime dateInJkt) {
        ZonedDateTime ldtJKTZoned = dateInJkt.atZone(ZoneId.of("Asia/Jakarta"));
        ZonedDateTime utcZoned = ldtJKTZoned.withZoneSameInstant(ZoneOffset.UTC);
        return LocalDateTime.from(utcZoned);
    }
}
