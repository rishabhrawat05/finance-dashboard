package com.finance.finance_dashboard.service;

import com.finance.finance_dashboard.dto.request.TransactionRequest;
import com.finance.finance_dashboard.dto.response.TransactionResponse;
import com.finance.finance_dashboard.enums.Category;
import com.finance.finance_dashboard.enums.Type;
import com.finance.finance_dashboard.exception.TransactionNotFoundException;
import com.finance.finance_dashboard.exception.TransactionTypeEmptyException;
import com.finance.finance_dashboard.model.Transaction;
import com.finance.finance_dashboard.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionResponse createTransaction(TransactionRequest transactionRequest) {
        Transaction transaction = new Transaction();
        transaction.setAmount(transactionRequest.amount());
        Category category = switch (transactionRequest.category().trim().toUpperCase()){
            case "FOOD" -> Category.FOOD;
            case "RENT" -> Category.RENT;
            case "TRANSPORT" -> Category.TRANSPORT;
            case "ENTERTAINMENT" -> Category.ENTERTAINMENT;
            case "SALARY" -> Category.SALARY;
            default -> null;

        };
        if(transactionRequest.type().isEmpty()){
            throw new TransactionTypeEmptyException("Transaction type cannot be empty");
        }
        Type type = switch(transactionRequest.type().trim().toUpperCase()){
            case "INCOME" -> Type.INCOME;
            case "EXPENSE" -> Type.EXPENSE;
            default -> null;
        };
        transaction.setType(type);
        transaction.setCategory(category);
        transaction.setDescription(transactionRequest.description());
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setUpdatedAt(LocalDateTime.now());
        Transaction savedTransaction = transactionRepository.save(transaction);
        return transactionToTransactionResponse(savedTransaction);

    }

    @Transactional
    public TransactionResponse updateTransaction(UUID id, TransactionRequest transactionRequest) {
        if(transactionRepository.existsById(id)){
            Transaction transaction = transactionRepository.findById(id).get();
            if(transactionRequest.description() != null){
                transaction.setDescription(transactionRequest.description());
            }
            if(transactionRequest.amount() != null){
                transaction.setAmount(transactionRequest.amount());
            }
            if(transactionRequest.category() != null){
                Category category = switch (transactionRequest.category().trim().toUpperCase()){
                    case "FOOD" -> Category.FOOD;
                    case "RENT" -> Category.RENT;
                    case "TRANSPORT" -> Category.TRANSPORT;
                    case "ENTERTAINMENT" -> Category.ENTERTAINMENT;
                    case "SALARY" -> Category.SALARY;
                    default -> null;

                };
                transaction.setCategory(category);
            }
            if(transactionRequest.type() != null){
                Type type = switch(transactionRequest.type().trim().toUpperCase()){
                    case "INCOME" -> Type.INCOME;
                    case "EXPENSE" -> Type.EXPENSE;
                    default -> null;
                };
                transaction.setType(type);
            }
            transaction.setUpdatedAt(LocalDateTime.now());
            Transaction savedTransaction = transactionRepository.save(transaction);
            return  transactionToTransactionResponse(savedTransaction);
        }
        else{
            throw new TransactionNotFoundException("Transaction Not Found with id: " + id);
        }
    }

    public void deleteTransaction(UUID id) {
        if(transactionRepository.existsById(id)){
            Transaction transaction = transactionRepository.findById(id).get();
            // soft delete
            transaction.setDeletedAt(LocalDateTime.now());
            transactionRepository.save(transaction);
        }
        else{
            throw new TransactionNotFoundException("Transaction Not Found with id: " + id);
        }
    }

    public TransactionResponse getTransactionById(UUID id){
        if(!transactionRepository.existsById(id)){
            throw new TransactionNotFoundException("Transaction Not Found with id: " + id);
        }
        else{
            Transaction transaction = transactionRepository.findById(id).get();
            return transactionToTransactionResponse(transaction);
        }
    }

    public Page<TransactionResponse> getAllTransactions(Pageable pageable){
        return transactionRepository.findAll(pageable).map(this::transactionToTransactionResponse);
    }

    public TransactionResponse transactionToTransactionResponse(Transaction transaction){
        return new TransactionResponse(transaction.getId(), transaction.getAmount(),
                transaction.getType().toString(), transaction.getDescription(),
                transaction.getCategory().toString(), transaction.getCreatedAt(),
                transaction.getUpdatedAt(), transaction.getDeletedAt());

    }

}
