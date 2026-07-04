package com.example.internshipmanager.models.dto.request.admin.criteria;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EditCriteriaRequest {
    @NotBlank(message = "Không đuợc để trống tên tiêu chí")
    private String criterionName;

    private String description;

    @NotNull(message = "Không được để trống điểm cao nhất")
    @Min(value = 0, message = "Số điểm phải lớn hơn 0")
    private double maxScore;
}
