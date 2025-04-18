package ru.fishkov.vacationcalculator.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.fishkov.vacationcalculator.dto.VacationPayAmountDto;
import ru.fishkov.vacationcalculator.dto.VacationPayAmountRequestDto;
import ru.fishkov.vacationcalculator.service.CalculatorService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/calculacte")
public class CalculatorController {
    private final CalculatorService calculatorService;

    @GetMapping
    public VacationPayAmountDto getVacationPayAmount(@Valid VacationPayAmountRequestDto vacationPayAmountRequestDto) {
        log.debug("GET /calculacte with params: {}", vacationPayAmountRequestDto);
        return calculatorService.getVacationPayAmount(vacationPayAmountRequestDto);
    }
}