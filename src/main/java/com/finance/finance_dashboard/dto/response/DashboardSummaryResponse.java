package com.finance.finance_dashboard.dto.response;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public record DashboardSummaryResponse(BigDecimal totalIncome, BigDecimal totalExpenses, BigDecimal netBalance, List<Object[]> categoryWiseTotal) {
}
