package com.example.internshipmanager.controller;

import com.example.internshipmanager.models.dto.request.admin.user.CreateUserRequest;
import com.example.internshipmanager.models.dto.request.admin.user.EditRoleRequest;
import com.example.internshipmanager.models.dto.request.admin.user.EditStatusRequest;
import com.example.internshipmanager.models.dto.request.admin.user.EditUserRequest;
import com.example.internshipmanager.models.dto.response.DataResponse;
import com.example.internshipmanager.models.entity.User;
import com.example.internshipmanager.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @GetMapping
    public ResponseEntity<DataResponse<List<User>>> getAllUser() {
        return new ResponseEntity<>(
                new DataResponse<>(
                        true,
                        "Tìm thấy " + adminService.getAllUser().size() + " người dùng",
                        adminService.getAllUser(),
                        null,
                        LocalDateTime.now()
                ), HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataResponse<User>> getUserById(@PathVariable Integer id) {
        return new ResponseEntity<>(
                new DataResponse<>(
                        true,
                        "Tìm thấy tài khoản có id " + id,
                        adminService.getUserById(id),
                        null,
                        LocalDateTime.now()
                ), HttpStatus.FOUND
        );
    }

    @PostMapping
    public ResponseEntity<DataResponse<User>> createUser (
            @Valid @RequestBody CreateUserRequest createUserRequest) {

        return new ResponseEntity<>(
                new DataResponse<>(
                        true,
                        "Đã tạo người dùng",
                        adminService.createUser(createUserRequest),
                        null,
                        LocalDateTime.now()
                ), HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<DataResponse<User>> editUser (
            @PathVariable Integer id, @Valid @RequestBody EditUserRequest editUserRequest) {

        return new ResponseEntity<>(
                new DataResponse<>(
                        true,
                        "Đã cập nhật người dùng",
                        adminService.editUser(id, editUserRequest),
                        null,
                        LocalDateTime.now()
                ), HttpStatus.OK
        );
    }

    @PutMapping("/{id}/role")
    public ResponseEntity<DataResponse<User>> editRole (
            @PathVariable Integer id, @Valid @RequestBody EditRoleRequest editRoleRequest) {
        return new ResponseEntity<>(
                new DataResponse<>(
                        true,
                        "Đã cập nhật người dùng",
                        adminService.editRole(id, editRoleRequest),
                        null,
                        LocalDateTime.now()
                ), HttpStatus.OK
        );
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<DataResponse<User>> editStatus (
            @PathVariable Integer id, @Valid @RequestBody EditStatusRequest editStatusRequest) {

        return new ResponseEntity<>(
                new DataResponse<>(
                        true,
                        "Đã cập nhật trạng thái người dùng",
                        adminService.editStatus(id, editStatusRequest),
                        null,
                        LocalDateTime.now()
                ), HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DataResponse<?>> deleteStatus(@PathVariable Integer id) {

        adminService.deleteUser(id);

        return new ResponseEntity<>(
                new DataResponse<>(
                        true,
                        "Đã xóa người dùng",
                        null,
                        null,
                        LocalDateTime.now()
                ), HttpStatus.OK
        );
    }
}
