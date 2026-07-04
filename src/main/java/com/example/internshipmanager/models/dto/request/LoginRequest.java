package com.example.internshipmanager.models.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @NotBlank(message = "Không được để trống Username")
    @Length(max = 50, message = "Tên đăng nhập không vượt quá 50 ký tự")
    private String username;

    @NotBlank(message = "Không được để trống password")
    private String password;
}
