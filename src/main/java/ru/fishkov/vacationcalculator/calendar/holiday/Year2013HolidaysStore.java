package ru.fishkov.vacationcalculator.calendar.holiday;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Year2013HolidaysStore implements YearHolidaysStore {
    private final List<Integer> holidays;

    public Year2013HolidaysStore() {
        holidays = new ArrayList<>();
        holidays.add(100 + 1);
        holidays.add(100 + 2);
        holidays.add(100 + 3);
        holidays.add(100 + 4);
        holidays.add(100 + 5);
        holidays.add(100 + 6);
        holidays.add(100 + 7);
        holidays.add(100 + 8);
        holidays.add(2 * 100 + 23);
        holidays.add(3 * 100 + 8);
        holidays.add(5 * 100 + 1);
        holidays.add(5 * 100 + 9);
        holidays.add(6 * 100 + 12);
        holidays.add(11 * 100 + 4);
    }

    @Override
    public int getMinHandledYear() {
        return 2013;
    }

    @Override
    public List<Integer> getHolidays() {
        return holidays;
    }
}