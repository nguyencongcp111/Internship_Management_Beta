package com.example.internshipmanager.models.dto.request.general;

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
public class EditMentorRequest {
    private String fullName;
    @NotBlank(message = "Không được để trống email")
    @Email
    private String email;


    @NotBlank(message = "Không được để trống số điện thoại")
    private String phoneNumber;
}
