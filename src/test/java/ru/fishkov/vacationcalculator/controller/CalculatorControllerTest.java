package ru.fishkov.vacationcalculator.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.fishkov.vacationcalculator.dto.VacationPayAmountDto;
import ru.fishkov.vacationcalculator.dto.VacationPayAmountRequestDto;
import ru.fishkov.vacationcalculator.service.CalculatorService;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CalculatorController.class)
class CalculatorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CalculatorService calculatorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void objectMapper_StartDateAndEndDate_DeserializedDates() throws Exception {
        // Given
        String json = "{ \"avg\": 1000.00, \"startDate\": \"2025-05-01\", \"endDate\": \"2025-05-10\" }";

        // When
        VacationPayAmountRequestDto dto = objectMapper.readValue(json, VacationPayAmountRequestDto.class);

        // Then
        assertThat(dto.getStartDate()).isEqualTo(LocalDate.of(2025, 5, 1));
        assertThat(dto.getEndDate()).isEqualTo(LocalDate.of(2025, 5, 10));
    }

    @Test
    void objectMapper_DurationAndAvgInsteadOfAvgSalary_DeserializedDurationAndAvgToAvgSalary() throws Exception {
        // Given
        String json = "{ \"avg\": 123456.78, \"duration\": 5 }";

        // When
        VacationPayAmountRequestDto dto = objectMapper.readValue(json, VacationPayAmountRequestDto.class);

        assertThat(dto.getAvgSalary()).isEqualByComparingTo(BigDecimal.valueOf(123456.78));
        assertThat(dto.getDuration()).isEqualTo(5);
    }

    @Test
    void calculacte_NoParams_FailBadRequest() throws Exception {
        // When / Then
        mockMvc.perform(get("/calculacte"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void calculacte_BothDurationAndDates_FailBadRequest() throws Exception {
        // When / Then
        mockMvc.perform(get("/calculacte")
                        .param("avg", "100")
                        .param("duration", "10")
                        .param("startDate", "2024-12-10")
                        .param("endDate", "2024-12-12"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void calculacte_InvalidAvg_FailBadRequest() throws Exception {
        // When / Then
        mockMvc.perform(get("/calculacte")
                        .param("avg", "0")
                        .param("duration", "10"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/calculacte")
                        .param("avg", "-100")
                        .param("duration", "10"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void calculacte_InvalidDuration_FailBadRequest() throws Exception {
        // When / Then
        mockMvc.perform(get("/calculacte")
                        .param("avg", "100")
                        .param("duration", "0"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/calculacte")
                        .param("avg", "100")
                        .param("duration", "-1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void calculacte_AvgSalaryAndDuration_returnVacationPayResult() throws Exception {
        // Given
        VacationPayAmountRequestDto request = VacationPayAmountRequestDto.builder()
                .avgSalary(new BigDecimal("50000"))
                .duration(10)
                .build();

        VacationPayAmountDto response = new VacationPayAmountDto(BigDecimal.valueOf(20000));
        Mockito.when(calculatorService.getVacationPayAmount(Mockito.any())).thenReturn(response);

        // When / Then
        mockMvc.perform(get("/calculacte")
                        .param("avg", request.getAvgSalary().toString())
                        .param("duration", request.getDuration().toString()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)))
                .andExpect(jsonPath("$.result").value(20000));;
    }
}