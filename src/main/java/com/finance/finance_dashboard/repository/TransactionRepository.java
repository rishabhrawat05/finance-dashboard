package com.finance.finance_dashboard.repository;

import com.finance.finance_dashboard.enums.Category;
import com.finance.finance_dashboard.model.Transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    @Query(
            """
    SELECT SUM(t.amount)
    FROM Transaction t
    WHERE t.type = "INCOME"
    AND t.deletedAt IS NULL
"""
    )
    BigDecimal findTotalIncome();

    @Query(
            """
    SELECT SUM(t.amount)
    FROM Transaction t
    WHERE t.type = "EXPENSE"
    AND t.deletedAt IS NULL
"""
    )
    BigDecimal findTotalExpense();

    @Query(
            """
    SELECT t.category, SUM(t.amount)
    FROM Transaction t
    WHERE t.deletedAt IS NULL
    GROUP BY t.category
"""
    )
    List<Object[]> getCategoryWiseTotal();


    @Query(
            """
    SELECT SUM(t.amount)
    FROM Transaction t
    WHERE (t.type = "INCOME"
    AND t.category = :category
    AND t.createdAt BETWEEN :startDate AND :endDate)
    AND t.deletedAt IS NULL
"""
    )
    BigDecimal findTotalIncomeByCategoryAndDate(Category category, LocalDate startDate, LocalDate endDate);

    @Query(
            """
    SELECT SUM(t.amount)
    FROM Transaction t
    WHERE (t.type = "EXPENSE"
    AND t.category = :category
    AND t.createdAt BETWEEN :startDate AND :endDate)
    AND t.deletedAt IS NULL
"""
    )
    BigDecimal findTotalExpenseByCategoryAndDate(Category category, LocalDate startDate, LocalDate endDate);

    @Query(
            """
    SELECT t.category, SUM(t.amount)
    FROM Transaction t
    WHERE (t.category = :category
    AND t.createdAt BETWEEN :startDate AND :endDate)
    AND t.deletedAt IS NULL
    GROUP BY t.category
"""
    )
    List<Object[]> getCategoryWiseTotalByCategoryAndDate(Category category, LocalDate startDate, LocalDate endDate);


    @Query(
            """
    SELECT SUM(t.amount)
    FROM Transaction t
    WHERE (t.type = "INCOME"
    AND t.category = :category)
    AND t.deletedAt IS NULL
"""
    )
    BigDecimal findTotalIncomeByCategory(Category category);

    @Query(
            """
    SELECT SUM(t.amount)
    FROM Transaction t
    WHERE (t.type = "EXPENSE"
    AND t.category = :category)
    AND t.deletedAt IS NULL
"""
    )
    BigDecimal findTotalExpenseByCategory(Category category);

    @Query(
            """
    SELECT t.category, SUM(t.amount)
    FROM Transaction t
    WHERE (t.category = :category)
    AND t.deletedAt IS NULL
    GROUP BY t.category
"""
    )
    List<Object[]> getCategoryWiseTotalByCategory(Category category);


}
