package com.example.internshipmanager.models.dto.request.admin.user;

import com.example.internshipmanager.models.entity.Roles;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EditRoleRequest {
    @NotNull(message = "Không được để trống role")
    private Roles role;
}
