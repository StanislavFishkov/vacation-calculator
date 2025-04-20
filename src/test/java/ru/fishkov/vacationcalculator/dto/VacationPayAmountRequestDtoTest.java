package ru.fishkov.vacationcalculator.dto;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class VacationPayAmountRequestDtoTest {
    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validate_SalaryIsNull_Fails() {
        // Given
        VacationPayAmountRequestDto dto = VacationPayAmountRequestDto.builder()
                .duration(5)
                .build();

        // When
        Set<ConstraintViolation<VacationPayAmountRequestDto>> violations = validator.validate(dto);

        // Then
        assertThat(violations)
                .anyMatch(v -> v.getPropertyPath().toString().equals("avgSalary"));
    }

    @Test
    void validate_AvgSalaryIsZero_Fails() {
        // Given
        VacationPayAmountRequestDto dto = VacationPayAmountRequestDto.builder()
                .avgSalary(BigDecimal.ZERO)
                .duration(5)
                .build();

        // When
        Set<ConstraintViolation<VacationPayAmountRequestDto>> violations = validator.validate(dto);

        // Then
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("avgSalary"));
    }

    @Test
    void validate_DurationIsZero_Fails() {
        // Given
        VacationPayAmountRequestDto dto = VacationPayAmountRequestDto.builder()
                .avgSalary(BigDecimal.valueOf(50000))
                .duration(0)
                .build();

        // When
        Set<ConstraintViolation<VacationPayAmountRequestDto>> violations = validator.validate(dto);

        // Then
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("duration"));
    }

    @Test
    void validate_EndDateIsBeforeStartDate_Fails() {
        // Given
        VacationPayAmountRequestDto dto = VacationPayAmountRequestDto.builder()
                .avgSalary(BigDecimal.valueOf(50000))
                .startDate(LocalDate.of(2025, 4, 20))
                .endDate(LocalDate.of(2025, 4, 19))
                .build();

        // When
        Set<ConstraintViolation<VacationPayAmountRequestDto>> violations = validator.validate(dto);

        // Then
        assertThat(violations).anyMatch(v -> v.getMessage().contains("Exactly one of 'duration' or valid date range"));
    }

    @Test
    void validate_BothDurationAndDates_Fails() {
        // Given
        VacationPayAmountRequestDto dto = VacationPayAmountRequestDto.builder()
                .avgSalary(BigDecimal.valueOf(50000))
                .duration(5)
                .startDate(LocalDate.of(2025, 4, 20))
                .endDate(LocalDate.of(2025, 4, 19))
                .build();

        // When
        Set<ConstraintViolation<VacationPayAmountRequestDto>> violations = validator.validate(dto);

        // Then
        assertThat(violations).anyMatch(v -> v.getMessage().contains("Exactly one of 'duration' or valid date range"));
    }

    @Test
    void validate_ValidAvgSalaryAndDuration_Passes() {
        // Given
        VacationPayAmountRequestDto dto = VacationPayAmountRequestDto.builder()
                .avgSalary(BigDecimal.valueOf(50000))
                .duration(10)
                .build();

        // When
        Set<ConstraintViolation<VacationPayAmountRequestDto>> violations = validator.validate(dto);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    void validate_ValidAvgSalaryAndDateRange_Passes() {
        // Given
        VacationPayAmountRequestDto dto = VacationPayAmountRequestDto.builder()
                .avgSalary(BigDecimal.valueOf(50000))
                .startDate(LocalDate.of(2025, 4, 1))
                .endDate(LocalDate.of(2025, 4, 10))
                .build();

        // When
        Set<ConstraintViolation<VacationPayAmountRequestDto>> violations = validator.validate(dto);

        // Then
        assertThat(violations).isEmpty();
    }
}