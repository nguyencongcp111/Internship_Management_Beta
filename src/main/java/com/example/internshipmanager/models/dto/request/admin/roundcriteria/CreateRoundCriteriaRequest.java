package com.example.internshipmanager.models.dto.request.admin.roundcriteria;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateRoundCriteriaRequest {
    @NotNull(message = "Không được để trống trọng số")
    @Min(value = 0, message = "Trọng số không được thấp hơn 0")
    private double weight;

    @NotNull(message = "Không được để trống id round")
    private Integer roundId;

    @NotNull(message = "Không được để trống id tiêu chí")
    private Integer criteriaId;
}
