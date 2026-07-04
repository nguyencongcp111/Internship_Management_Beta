package com.example.internshipmanager.service;

import com.example.internshipmanager.models.dto.request.LoginRequest;
import com.example.internshipmanager.models.dto.response.JWTResponse;
import com.example.internshipmanager.models.entity.User;
import jakarta.servlet.http.HttpSession;

public interface UserService {
    JWTResponse loginUser(LoginRequest loginRequest, HttpSession httpSession);

    User getUserById(Integer id);
}
