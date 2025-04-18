package ru.fishkov.vacationcalculator.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.fishkov.vacationcalculator.dto.VacationPayAmountDto;
import ru.fishkov.vacationcalculator.dto.VacationPayAmountRequestDto;

import java.math.BigDecimal;

@Slf4j
@Service
public class CalculatorServiceImpl implements CalculatorService {
    @Override
    public VacationPayAmountDto getVacationPayAmount(VacationPayAmountRequestDto vacationPayAmountRequestDto) {

        BigDecimal duration = BigDecimal.valueOf(vacationPayAmountRequestDto.getDuration());

        BigDecimal result = vacationPayAmountRequestDto.getAvgSalary().multiply(duration);

        return VacationPayAmountDto.builder()
                .result(result)
                .build();
    }
}
