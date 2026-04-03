package com.finance.finance_dashboard.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record RegisterRequest(
        @NotNull
        @Length(min = 1, max = 100)
        String name,
        @Email
        String email,
        @NotNull
        @Length(min = 8, max = 100)
        String password, String role) {
}
