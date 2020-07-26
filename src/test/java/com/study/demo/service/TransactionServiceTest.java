package com.study.demo.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.study.demo.entity.Transaction;
import com.study.demo.repository.TransactionRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    TransactionRepository transactionRepo;

    @InjectMocks
    TransactionService transactionService;

    @Test
    public void shouldReturnTheTransaction_whenTransactionIsSuccessful() {

        Transaction transaction = new Transaction();
        transactionService.transact(transaction);
        verify(transactionRepo).save(transaction);
    }

}