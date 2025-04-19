package ru.fishkov.vacationcalculator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.fishkov.vacationcalculator.calendar.BusinessCalendarService;
import ru.fishkov.vacationcalculator.dto.VacationPayAmountDto;
import ru.fishkov.vacationcalculator.dto.VacationPayAmountRequestDto;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class CalculatorServiceImpl implements CalculatorService {
    private final BusinessCalendarService businessCalendarService;

    @Override
    public VacationPayAmountDto getVacationPayAmount(VacationPayAmountRequestDto requestDto) {

        BigDecimal duration = BigDecimal.valueOf(
                requestDto.getDuration() == null ?
                        businessCalendarService.getWorkingDaysCount(requestDto.getStartDate(), requestDto.getEndDate())
                        : requestDto.getDuration()
        );

        BigDecimal result = requestDto.getAvgSalary().multiply(duration);
        log.debug("Vacation payment result is {} for request {}", result, requestDto);
        return VacationPayAmountDto.builder()
                .result(result)
                .build();
    }
}
