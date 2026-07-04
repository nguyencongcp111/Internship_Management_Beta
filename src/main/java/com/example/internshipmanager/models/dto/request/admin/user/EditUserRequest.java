package com.example.internshipmanager.models.dto.request.admin.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EditUserRequest {
    @NotNull(message = "Không được để trống họ và tên")
    @Length(max = 100, message = "Họ và tên tối đa 50 ký tự")
    private String fullName;

    @NotNull(message = "Không được để trống email")
    @Email(message = "Không đúng định dạng email")
    @Length(max = 100, message = "Email tối đa 100 ký tự")
    private String email;

    //@Pattern(regexp = "^0[35789]\\d{8}$", message = "Số điện thoại không đúng định dạng")
    private String phoneNumber;
}
