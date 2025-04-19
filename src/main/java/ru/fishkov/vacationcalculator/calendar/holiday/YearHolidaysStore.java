package ru.fishkov.vacationcalculator.calendar.holiday;

import java.util.List;

public interface YearHolidaysStore {
    int getMinHandledYear();

    List<Integer> getHolidays();
}
