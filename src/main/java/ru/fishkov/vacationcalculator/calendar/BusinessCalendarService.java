package ru.fishkov.vacationcalculator.calendar;

import java.time.LocalDate;

public interface BusinessCalendarService {
    long getWorkingDaysCount(LocalDate startDate, LocalDate endDate);
}