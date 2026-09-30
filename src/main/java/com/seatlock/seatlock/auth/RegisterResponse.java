package com.seatlock.seatlock.auth;

import com.seatlock.seatlock.user.Role;
import com.seatlock.seatlock.user.User;
import java.time.Instant;

public record RegisterResponse(Long id, String email, Role role, Instant createdAt) {

    public static RegisterResponse from(User user) {
        return new RegisterResponse(user.getId(), user.getEmail(), user.getRole(), user.getCreatedAt());
    }
}