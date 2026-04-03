package com.finance.finance_dashboard.enums;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    VIEWER,
    ANALYST,
    ADMIN;

    @Override
    public @Nullable String getAuthority() {
        return name();
    }
}
