package com.example.internshipmanager.service.impl;

import com.example.internshipmanager.exception.ResourceNotFoundException;
import com.example.internshipmanager.exception.UnexpectedRoleException;
import com.example.internshipmanager.models.dto.request.LoginRequest;
import com.example.internshipmanager.models.dto.response.JWTResponse;
import com.example.internshipmanager.models.entity.User;
import com.example.internshipmanager.repository.UserRepository;
import com.example.internshipmanager.security.jwt.JwtProvider;
import com.example.internshipmanager.security.principal.UserPrincipal;
import com.example.internshipmanager.service.UserService;
import com.example.internshipmanager.service.JwtBlacklistService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final JwtBlacklistService jwtBlacklistService;

    @Override
    public JWTResponse loginUser(LoginRequest loginRequest, HttpSession httpSession) {
        try {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

            String role = userPrincipal.getAuthorities()
                    .stream()
                    .findFirst()
                    .map(GrantedAuthority::getAuthority)
                    .orElseThrow(
                            () -> new IllegalArgumentException("User không có role")
                    );

            String token = jwtProvider.generateToken(userPrincipal.getUsername(), role);



            return JWTResponse.builder()
                    .username(userPrincipal.getUsername())
                    .type("Bearer")
                    .accessToken(token)
                    .build();
        } catch (DisabledException e) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Tài khoản không hoạt động"
            );
        }
    }

    @Override
    public User getUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không thể tìm thấy người dùng"));
    }

    @Override
    @Transactional
    public void logout (String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid Authorization Header");
        }

        String token = authorization.substring(7);

        Claims claims = jwtProvider.getClaimsFromToken(token);

        String jti = claims.getId();

        Instant expiry = claims.getExpiration().toInstant();

        jwtBlacklistService.blacklist(jti, expiry);
    }
}
