package com.finance.finance_dashboard.controller;

import com.finance.finance_dashboard.dto.response.DashboardSummaryResponse;
import com.finance.finance_dashboard.enums.Category;
import com.finance.finance_dashboard.service.DashboardSummaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "APIs for dashboard summary and analytics")
public class DashboardSummaryController {

    private final DashboardSummaryService dashboardSummaryService;

    @Operation(
            summary = "Get overall dashboard summary",
            description = "Returns aggregated dashboard data including total income, total expenses, net balance and category wise total"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Summary fetched successfully",
                    content = @Content(schema = @Schema(implementation = DashboardSummaryResponse.class))),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/summary")
    @PreAuthorize("hasAnyRole('VIEWER', 'ANALYST', 'ADMIN')")
    public ResponseEntity<DashboardSummaryResponse> getDashboardSummary() {
        return ResponseEntity.ok(dashboardSummaryService.getDashboardSummary());
    }

    @Operation(
            summary = "Filter dashboard by category and date range",
            description = "Returns dashboard summary filtered by category and date range"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Filtered summary fetched successfully",
                    content = @Content(schema = @Schema(implementation = DashboardSummaryResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input parameters"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/filter")
    @PreAuthorize("hasAnyRole('VIEWER', 'ANALYST', 'ADMIN')")
    public ResponseEntity<DashboardSummaryResponse> getDashboardSummaryByCategoryAndDate(@RequestParam String category, @RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Dates must not be null");
        }

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        if (endDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("End date cannot exceed current date");
        }

        Category categoryEnum = getCategoryFromString(category);

        return ResponseEntity.ok(dashboardSummaryService.getDashboardSummaryByCategoryAndDate(categoryEnum, startDate, endDate));
    }



    @Operation(
            summary = "Get dashboard by category",
            description = "Returns dashboard summary filtered by a specific category"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category summary fetched successfully",
                    content = @Content(schema = @Schema(implementation = DashboardSummaryResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid category"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/category")
    @PreAuthorize("hasAnyRole('VIEWER', 'ANALYST', 'ADMIN')")
    public ResponseEntity<DashboardSummaryResponse> getDashboardSummaryByCategory(@RequestParam String category) {
        Category categoryEnum = getCategoryFromString(category);
        return ResponseEntity.ok(dashboardSummaryService.getDashboardSummaryByCategory(categoryEnum));
    }

    private Category getCategoryFromString(String category) {
        return switch (category.trim().toUpperCase()){
            case "FOOD" -> Category.FOOD;
            case "RENT" -> Category.RENT;
            case "TRANSPORT" -> Category.TRANSPORT;
            case "ENTERTAINMENT" -> Category.ENTERTAINMENT;
            case "SALARY" -> Category.SALARY;
            default -> null;

        };
    }

}
