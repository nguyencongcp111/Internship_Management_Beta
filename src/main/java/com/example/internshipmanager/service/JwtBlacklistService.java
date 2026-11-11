package com.example.internshipmanager.service;

import java.time.Instant;

public interface JwtBlacklistService {
    void blacklist (String jti, Instant expiry);

    boolean isBlacklisted (String jti);
}
