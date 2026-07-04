package com.example.internshipmanager.models.dto.request.admin.phase;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class CreatePhaseRequest {
    @NotBlank(message = "Không được để trống tên giai đoạn")
    private String phaseName;
    @NotNull(message = "Không được để trống ngày bắt đầu")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate startDate;
    @NotNull(message = "Không được để trống ngày kết thúc")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate endDate;

    private String description;
}
