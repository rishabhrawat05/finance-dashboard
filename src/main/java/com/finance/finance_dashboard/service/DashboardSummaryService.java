package com.finance.finance_dashboard.service;

import com.finance.finance_dashboard.dto.response.DashboardSummaryResponse;
import com.finance.finance_dashboard.enums.Category;
import com.finance.finance_dashboard.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardSummaryService {

    private final TransactionRepository transactionRepository;

    public DashboardSummaryResponse getDashboardSummary(){
        BigDecimal totalIncome = transactionRepository.findTotalIncome();
        BigDecimal totalExpense = transactionRepository.findTotalExpense();
        BigDecimal netBalance = totalIncome.subtract(totalExpense);
        List<Object[]> categoryWiseTotal = transactionRepository.getCategoryWiseTotal();

        return new DashboardSummaryResponse(
                totalIncome, totalExpense, netBalance, categoryWiseTotal
        );
    }

    public DashboardSummaryResponse getDashboardSummaryByCategoryAndDate(Category category, LocalDate startDate, LocalDate endDate){
        BigDecimal totalIncome = transactionRepository.findTotalIncomeByCategoryAndDate(category, startDate, endDate);
        BigDecimal totalExpense = transactionRepository.findTotalExpenseByCategoryAndDate(category, startDate, endDate);
        BigDecimal netBalance = totalIncome.subtract(totalExpense);
        List<Object[]> categoryWiseTotal = transactionRepository.getCategoryWiseTotalByCategoryAndDate(category, startDate, endDate);

        return new DashboardSummaryResponse(
                totalIncome, totalExpense, netBalance, categoryWiseTotal
        );
    }

    public DashboardSummaryResponse getDashboardSummaryByCategory(Category category){
        BigDecimal totalIncome = transactionRepository.findTotalIncomeByCategory(category);
        BigDecimal totalExpense = transactionRepository.findTotalExpenseByCategory(category);
        BigDecimal netBalance = totalIncome.subtract(totalExpense);
        List<Object[]> categoryWiseTotal = transactionRepository.getCategoryWiseTotalByCategory(category);

        return new DashboardSummaryResponse(
                totalIncome, totalExpense, netBalance, categoryWiseTotal
        );
    }
}
