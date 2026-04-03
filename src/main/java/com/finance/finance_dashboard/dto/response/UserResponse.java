package com.finance.finance_dashboard.dto.response;


import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponse(UUID id, String name, String email, String role, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
}
