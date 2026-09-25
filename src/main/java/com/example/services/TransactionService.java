package com.example.services;

import com.example.dto.TransactionDTO;
import com.example.models.Entity.Transaction;
import com.example.repository.Transaction.TransactionRepository;

public class TransactionService {
    private TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository){
        this.transactionRepository = transactionRepository;
    }

    public Transaction save(TransactionDTO transactionDTO){
        Transaction transaction = new Transaction();
        transaction.setAmount(transactionDTO.getAmount());
        return transactionRepository.save(transaction);
    }
}
