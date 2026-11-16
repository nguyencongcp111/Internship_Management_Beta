package com.example.internshipmanager.models.dto.request.admin.student;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class AdminEditStudentRequest {
    private String major;

    private String className;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    private String address;

    private String fullName;
    @NotBlank(message = "Không được để trống email")
    @Email
    private String email;


    @NotBlank(message = "Không được để trống số điện thoại")
    private String phoneNumber;
}
