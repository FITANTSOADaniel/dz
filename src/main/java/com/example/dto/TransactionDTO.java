package com.example.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor 
@AllArgsConstructor
@Getter 
@Setter 
public class TransactionDTO {
    private int fromUserId;
    private int toUserId;
    private BigDecimal amount;
    private LocalDateTime createdAt;
}
