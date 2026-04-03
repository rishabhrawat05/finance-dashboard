package com.finance.finance_dashboard.dto.request;

import jakarta.validation.constraints.Min;

import java.math.BigDecimal;

public record TransactionRequest(@Min(0) BigDecimal amount, String type, String description, String category) {
}
