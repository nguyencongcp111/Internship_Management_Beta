package com.example.internshipmanager.models.dto.request.admin.student;

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
public class CreateStudentRequest {
    @NotBlank(message = "Không được để trống mã sinh viên")
    private String studentCode;

    private String major;

    private String className;

    @NotNull(message = "Không được để trống ngày sinh")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    private String address;

    private Integer userId;
}
