package ru.fishkov.vacationcalculator.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import ru.fishkov.vacationcalculator.util.DateUtil;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VacationPayAmountRequestDto {

    @NotNull
    @DecimalMin(value = "0", inclusive = false)
    BigDecimal avgSalary;

    @Min(1)
    Integer duration;

    @DateTimeFormat(pattern = DateUtil.DATE_FORMAT)
    LocalDate startDate;
    @DateTimeFormat(pattern = DateUtil.DATE_FORMAT)
    LocalDate endDate;

    @JsonProperty("avg")
    public void setAvg(BigDecimal avg) {
        this.avgSalary = avg;
    }

    @AssertTrue(message = "Exactly one of 'duration' or valid date range ('startDate' and 'endDate') must be provided.")
    public boolean isDurationValid() {
        if (duration == null) {
            return startDate != null && endDate != null && startDate.isBefore(endDate);
        } else {
            return startDate == null && endDate == null;
        }
    }
}