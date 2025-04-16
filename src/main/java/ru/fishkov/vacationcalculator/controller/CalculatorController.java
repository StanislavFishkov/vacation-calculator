package ru.fishkov.vacationcalculator.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/calculacte")
public class CalculatorController {
    @GetMapping
    public String getVacationPayAmount() {
        return "Hello";
    }
}