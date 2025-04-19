package ru.fishkov.vacationcalculator.calendar.holiday;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Year2005HolidaysStore implements YearHolidaysStore {
    private final List<Integer> holidays;

    public Year2005HolidaysStore() {
        holidays = new ArrayList<>();
        holidays.add(dayMonthHash(1,1));
        holidays.add(dayMonthHash(1,2));
        holidays.add(dayMonthHash(1,3));
        holidays.add(dayMonthHash(1,4));
        holidays.add(dayMonthHash(1,5));
        holidays.add(dayMonthHash(1,7));
        holidays.add(dayMonthHash(2,23));
        holidays.add(dayMonthHash(3,8));
        holidays.add(dayMonthHash(5,1));
        holidays.add(dayMonthHash(5,9));
        holidays.add(dayMonthHash(6,12));
        holidays.add(dayMonthHash(11,4));
    }

    @Override
    public int getMinHandledYear() {
        return 2005;
    }

    @Override
    public List<Integer> getHolidays() {
        return holidays;
    }
}