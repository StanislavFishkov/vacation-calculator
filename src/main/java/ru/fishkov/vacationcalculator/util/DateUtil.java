package ru.fishkov.vacationcalculator.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class DateUtil {
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);

    public static LocalDate currentDate() {
        return LocalDate.now();
    }
}