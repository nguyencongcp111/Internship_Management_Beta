package com.example.internshipmanager.controller;

import com.example.internshipmanager.models.dto.request.LoginRequest;
import com.example.internshipmanager.models.dto.response.DataResponse;
import com.example.internshipmanager.models.dto.response.JWTResponse;
import com.example.internshipmanager.models.entity.User;
import com.example.internshipmanager.security.principal.UserPrincipal;
import com.example.internshipmanager.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<DataResponse<JWTResponse>> login(
            @Valid @RequestBody LoginRequest loginRequest,
            HttpSession httpSession
    ) {
        return new ResponseEntity<>(
                new DataResponse<>(
                        true,
                        "Đăng nhập thành công",
                        userService.loginUser(loginRequest, httpSession),
                        null,
                        LocalDateTime.now()
                ), HttpStatus.OK
        );
    }

    @GetMapping("/me")
    public ResponseEntity<DataResponse<User>> me(@AuthenticationPrincipal UserPrincipal userPrincipal) {

        return new ResponseEntity<>(
                new DataResponse<>(
                        true,
                        "Hồ sơ của bạn: ",
                        userPrincipal.getUser(),
                        null,
                        LocalDateTime.now()
                ), HttpStatus.OK
        );
    }
}