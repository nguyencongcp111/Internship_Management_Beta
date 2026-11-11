package com.example.internshipmanager.service;

import com.example.internshipmanager.models.dto.request.LoginRequest;
import com.example.internshipmanager.models.dto.response.JWTResponse;
import com.example.internshipmanager.models.entity.User;
import jakarta.servlet.http.HttpSession;

<<<<<<< HEAD
=======
import java.time.Instant;

>>>>>>> 2cf64d9 (fixed auth, gan nham roundId)
public interface UserService {
    JWTResponse loginUser(LoginRequest loginRequest, HttpSession httpSession);

    User getUserById(Integer id);
<<<<<<< HEAD
=======

    void logout (String authorization);
>>>>>>> 2cf64d9 (fixed auth, gan nham roundId)
}
