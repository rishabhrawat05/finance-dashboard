package com.finance.finance_dashboard.controller;

import com.finance.finance_dashboard.dto.request.TransactionRequest;
import com.finance.finance_dashboard.dto.response.TransactionResponse;
import com.finance.finance_dashboard.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@Tag(name = "Transaction", description = "APIs for managing financial transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @Operation(summary = "Create transaction", description = "Creates a new transaction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transaction created successfully",
                    content = @Content(schema = @Schema(implementation = TransactionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TransactionResponse> createTransaction(@Valid @RequestBody TransactionRequest transactionRequest) {
        return ResponseEntity.created(URI.create("/api/transaction/")).body(transactionService.createTransaction(transactionRequest));
    }

    @Operation(summary = "Update transaction", description = "Updates an existing transaction by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction updated successfully",
                    content = @Content(schema = @Schema(implementation = TransactionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Transaction not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TransactionResponse> updateTransaction(@RequestParam UUID id, @Valid @RequestBody TransactionRequest transactionRequest) {
        return ResponseEntity.ok(transactionService.updateTransaction(id, transactionRequest));
    }

    @Operation(summary = "Update transaction", description = "Updates an existing transaction by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction updated successfully",
                    content = @Content(schema = @Schema(implementation = TransactionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Transaction not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTransaction(@RequestParam UUID id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get transaction by ID", description = "Fetch a single transaction using its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction fetched successfully",
                    content = @Content(schema = @Schema(implementation = TransactionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Transaction not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TransactionResponse> getTransactionById(@RequestParam UUID id){
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }

    @Operation(summary = "Get all transactions (paginated)",
            description = "Returns paginated list of transactions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transactions fetched successfully",
                    content = @Content(schema = @Schema(implementation = TransactionResponse.class))),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST')")
    public Page<TransactionResponse> getAllTransactions(@RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        return  transactionService.getAllTransactions(pageable);
    }


}
