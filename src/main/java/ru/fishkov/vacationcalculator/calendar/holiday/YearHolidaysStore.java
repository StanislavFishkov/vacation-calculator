package ru.fishkov.vacationcalculator.calendar.holiday;

import java.time.LocalDate;
import java.util.List;

public interface YearHolidaysStore {
    int getMinHandledYear();

    List<Integer> getHolidays();

    default int dayMonthHash(LocalDate date) {
        return dayMonthHash(date.getMonthValue(), date.getDayOfMonth());
    }

    default int dayMonthHash(int month, int day) {
        return month * 100 + day;
    }
}