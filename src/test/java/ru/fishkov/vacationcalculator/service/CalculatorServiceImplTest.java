package ru.fishkov.vacationcalculator.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.fishkov.vacationcalculator.calendar.BusinessCalendarServiceImpl;
import ru.fishkov.vacationcalculator.dto.VacationPayAmountDto;
import ru.fishkov.vacationcalculator.dto.VacationPayAmountRequestDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class CalculatorServiceImplTest {
    @Mock
    private BusinessCalendarServiceImpl calendarService;

    @InjectMocks
    private CalculatorServiceImpl calculatorService;

    @ParameterizedTest
    @CsvSource({
            "0, 2, 0",
            "2, 0, 0",
            "2500.31, 3, 7500.93"
    })
    void getVacationPayAmount_DurationGiven_CorrectResultsBasedOnDuration(String avgSalary, int duration, String result) {
        // Given
        VacationPayAmountRequestDto requestDto = VacationPayAmountRequestDto.builder()
                .avgSalary(new BigDecimal(avgSalary))
                .duration(duration)
                .build();

        // When
        VacationPayAmountDto resultDto = calculatorService.getVacationPayAmount(requestDto);

        // Then
        assertThat(resultDto.getResult())
                .isEqualTo(result);

        Mockito.verify(calendarService, Mockito.never())
                .getWorkingDaysCount(any(LocalDate.class), any(LocalDate.class));
    }

    @ParameterizedTest
    @CsvSource({
            "0, 2012-05-04, 2025-04-19, 0",
            "1, 2024-08-09, 2024-08-09, 1",
            "234.56, 2025-03-09, 2025-03-18, 2345.60"
    })
    void getVacationPayAmount_DurationNotGiven_CorrectResultsBasedOnDates(String avgSalary,
                                                                          LocalDate startDate, LocalDate endDate,
                                                                          String result) {
        // Given
        VacationPayAmountRequestDto requestDto = VacationPayAmountRequestDto.builder()
                .avgSalary(new BigDecimal(avgSalary))
                .startDate(startDate)
                .endDate(endDate)
                .build();

        Mockito.when(calendarService.getWorkingDaysCount(startDate, endDate))
                .thenReturn(ChronoUnit.DAYS.between(startDate, endDate) + 1);

        // When
        VacationPayAmountDto resultDto = calculatorService.getVacationPayAmount(requestDto);

        // Then
        assertThat(resultDto.getResult())
                .isEqualTo(result);

        Mockito.verify(calendarService, Mockito.only())
                .getWorkingDaysCount(any(LocalDate.class), any(LocalDate.class));
    }
}