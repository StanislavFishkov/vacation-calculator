package ru.fishkov.vacationcalculator.service;

import ru.fishkov.vacationcalculator.dto.VacationPayAmountDto;
import ru.fishkov.vacationcalculator.dto.VacationPayAmountRequestDto;

public interface CalculatorService {
    VacationPayAmountDto getVacationPayAmount(VacationPayAmountRequestDto vacationPayAmountRequestDto);
}