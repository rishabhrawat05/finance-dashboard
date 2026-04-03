package com.finance.finance_dashboard.model;

import com.finance.finance_dashboard.enums.Category;
import com.finance.finance_dashboard.enums.Type;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.ReadOnlyProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Min(0)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private Type type;

    private String description;

    @Enumerated(EnumType.STRING)
    private Category category;

    private LocalDateTime createdAt;

    private LocalDateTime deletedAt;

    private LocalDateTime updatedAt;


}
