package com.finance.finance_dashboard.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionResponse(UUID id, BigDecimal amount, String type, String description, String category, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
}
