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
        if (endDate.isBefore(startDate))
            throw new ValidationException(String.format("End date %s is before start date %s", endDate, startDate));

        long result = Math.abs(ChronoUnit.DAYS.between(startDate, endDate)) + 1;

        int startYear = startDate.getYear();
        int endYear = endDate.getYear();

        YearHolidaysStore holidaysStoreEntry = Optional.ofNullable(holidaysByYears.floorEntry(startYear))
                .orElseThrow(() -> new ValidationException(String.format("There is no list of holidays for year %s", startYear)))
                .getValue();

        // add holidays from the start of the first year to the startDate to eliminate subtraction of the whole year holidays
        int start = holidaysStoreEntry.dayMonthHash(startDate);
        result += holidaysStoreEntry.getHolidays().stream().filter(h -> h < start).count();

        // subtract all holidays for the year startYear to the year endYear both inclusively
        int curYear = startYear;
        while (curYear <= endYear) {
            Map.Entry<Integer, YearHolidaysStore> holidaysStoreEntryNext = holidaysByYears.higherEntry(curYear);
            int nextChangeYear = endYear + 1;
            int curHolidaysCount = holidaysStoreEntry.getHolidays().size();
            if (holidaysStoreEntryNext != null && nextChangeYear > holidaysStoreEntryNext.getKey()) {
                nextChangeYear = holidaysStoreEntryNext.getKey();
                holidaysStoreEntry = holidaysStoreEntryNext.getValue();
            }
            result -= (long) (nextChangeYear - curYear) * curHolidaysCount;
            curYear = nextChangeYear;
        }

        // add holidays from the endDate to the end of the last year eliminate subtraction of the whole year holidays
        int end = holidaysStoreEntry.dayMonthHash(endDate);
        result += holidaysStoreEntry.getHolidays().stream().filter(h -> h > end).count();

        log.debug("Working days count result is {} for dates {}, {}", result, startDate, endDate);
        return result;
    }
}