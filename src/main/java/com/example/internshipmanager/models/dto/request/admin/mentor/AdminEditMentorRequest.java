package com.example.internshipmanager.models.dto.request.admin.mentor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AdminEditMentorRequest {
    private String department;
    private String academicRank;

    private String fullName;
    @NotBlank(message = "Không được để trống email")
    @Email
    private String email;


    @NotBlank(message = "Không được để trống số điện thoại")
    private String phoneNumber;
}
