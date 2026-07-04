package com.example.internshipmanager.models.dto.request.admin.round;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateRoundRequest {
    @NotBlank(message = "Không được để trống tên đợt")
    private String roundName;

    @NotNull(message = "Không được để trống id giai đoạn")
    private Integer phaseId;

    @NotNull(message = "Không được để trống ngày bắt đầu")
    private LocalDate startDate;

    @NotNull(message = "Không được để trống ngày kết thúc")
    private LocalDate endDate;

    private String description;
}
