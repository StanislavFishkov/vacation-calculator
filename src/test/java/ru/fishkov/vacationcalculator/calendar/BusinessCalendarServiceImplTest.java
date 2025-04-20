package ru.fishkov.vacationcalculator.calendar;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.fishkov.vacationcalculator.calendar.holiday.Year2005HolidaysStore;
import ru.fishkov.vacationcalculator.calendar.holiday.Year2013HolidaysStore;
import ru.fishkov.vacationcalculator.error.exception.ValidationException;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

class BusinessCalendarServiceImplTest {

    private static BusinessCalendarServiceImpl calendarService;

    @BeforeAll
    static void init() {
        calendarService = new BusinessCalendarServiceImpl(Set.of(
                new Year2005HolidaysStore(),
                new Year2013HolidaysStore()
        ));
    }

    @Test
    void getWorkingDaysCount_EndDateBeforeStartDate_ThrowException() {
        // Given
        LocalDate start = LocalDate.of(2005, 5, 10);
        LocalDate end = LocalDate.of(2005, 5, 1);

        // When
        Throwable thrown = catchThrowable(() -> calendarService.getWorkingDaysCount(start, end));

        // Then
        assertThat(thrown)
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("End date");
    }

    @Test
    void getWorkingDaysCount_YearNotHandled_ThrowException() {
        // Given
        BusinessCalendarServiceImpl emptyService = new BusinessCalendarServiceImpl(Set.of());

        LocalDate start = LocalDate.of(2005, 1, 1);
        LocalDate end = LocalDate.of(2005, 1, 2);

        // When
        Throwable thrown = catchThrowable(() -> emptyService.getWorkingDaysCount(start, end));

        // Then
        assertThat(thrown)
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("There is no list of holidays for year");
    }

    @ParameterizedTest
    @MethodSource("provideDatesAndExpectedCounts")
    void testWorkingDaysCount(TestData data) {
        // Given
        // provideDatesAndExpectedCounts()

        // When
        long actual = calendarService.getWorkingDaysCount(data.start, data.end);

        // Then
        assertThat(actual)
                .as("Expected %d working days between %s and %s, but got %d",
                        data.expected, data.start, data.end, actual)
                .isEqualTo(data.expected);
    }

    static Stream<TestData> provideDatesAndExpectedCounts() {
        return Stream.of(
                new TestData(LocalDate.of(2005, 1, 1), LocalDate.of(2005, 1, 1), 0),
                new TestData(LocalDate.of(2005, 1, 6), LocalDate.of(2005, 1, 6), 1),
                new TestData(LocalDate.of(2005, 1, 1), LocalDate.of(2005, 1, 10), 4),
                new TestData(LocalDate.of(2013, 1, 1), LocalDate.of(2013, 1, 10), 2),
                new TestData(LocalDate.of(2013, 1, 9), LocalDate.of(2013, 1, 9), 1),
                new TestData(LocalDate.of(2005, 12, 31), LocalDate.of(2013, 1, 8), 2474)
        );
    }

    static class TestData {
        final LocalDate start;
        final LocalDate end;
        final long expected;

        TestData(LocalDate start, LocalDate end, long expected) {
            this.start = start;
            this.end = end;
            this.expected = expected;
        }

        @Override
        public String toString() {
            return start + " -> " + end + " = " + expected;
        }
    }
}