package com.example.internshipmanager.models.dto.request.admin.user;

import com.example.internshipmanager.models.entity.Roles;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
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
public class CreateUserRequest {
    @NotNull(message = "Không được để trống username")
    @Length(max = 50, message = "Tên đăng nhập không vượt quá 50 ký tự")
    private String username;

    @NotNull(message = "Không được để trống mật khẩu")
    private String password;

    @NotNull(message = "Không được để trống họ và tên")
    @Length(max = 100, message = "Họ và tên tối đa 50 ký tự")
    private String fullName;

    @NotNull(message = "Không được để trống email")
    @Email(message = "Không đúng định dạng email")
    @Length(max = 100, message = "Email tối đa 100 ký tự")
    private String email;

<<<<<<< HEAD
    //@Pattern(regexp = "^0[35789]\\d{8}$", message = "Số điện thoại không đúng định dạng")
=======
    @Pattern(regexp = "^0[35789]\\d{8}$", message = "Số điện thoại không đúng định dạng")
>>>>>>> 2cf64d9 (fixed auth, gan nham roundId)
    private String phoneNumber;

    @NotNull(message = "Không được để trống role")
    private Roles role;

    private boolean isActive;
}
