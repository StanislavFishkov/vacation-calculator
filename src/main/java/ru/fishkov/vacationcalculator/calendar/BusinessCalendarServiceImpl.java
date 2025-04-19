package ru.fishkov.vacationcalculator.calendar;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.fishkov.vacationcalculator.calendar.holiday.YearHolidaysStore;
import ru.fishkov.vacationcalculator.error.exception.ValidationException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@Service
public class BusinessCalendarServiceImpl implements BusinessCalendarService {
    private final NavigableMap<Integer, YearHolidaysStore> holidaysByYears;

    public BusinessCalendarServiceImpl(Set<YearHolidaysStore> yearHolidaysStores) {
        holidaysByYears = new TreeMap<>();
        yearHolidaysStores.forEach(s -> holidaysByYears.put(s.getMinHandledYear(), s));
    }

    @Override
    public long getWorkingDaysCount(LocalDate startDate, LocalDate endDate) {
        long result = Math.abs(ChronoUnit.DAYS.between(startDate, endDate)) + 1;

        int startYear = startDate.getYear();
        int endYear = endDate.getYear();

        for (int y = startYear; y <= endYear; y++) {
            result -= getHolidaysForDateRange(y == startYear ? startDate : LocalDate.of(y, 1, 1),
                    y == endYear ? endDate : LocalDate.of(y, 12, 31), y);
        }
        log.debug("Working days count result is {} for dates {}, {}", result, startDate, endDate);
        return result;
    }

    private long getHolidaysForDateRange(LocalDate startDate, LocalDate endDate, Integer year) {
        YearHolidaysStore holidaysStoreEntry = Optional.ofNullable(holidaysByYears.floorEntry(year))
                .orElseThrow(() -> new ValidationException(String.format("There is no list of holidays for year %s", year)))
                .getValue();

        int start = startDate.getMonthValue() * 100 + startDate.getDayOfMonth();
        int end = endDate.getMonthValue() * 100 + endDate.getDayOfMonth();

        long result =holidaysStoreEntry.getHolidays().stream().filter(h -> h >= start && h <= end).count();
        log.debug("Holidays count result is {} for dates {}, {}", result, startDate, endDate);
        return holidaysStoreEntry.getHolidays().stream().filter(h -> h >= start && h <= end).count();
    }
}