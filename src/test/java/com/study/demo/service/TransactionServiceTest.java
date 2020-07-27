package com.study.demo.service;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.study.demo.entity.Transaction;
import com.study.demo.fixtures.transaction.TransactionFixtures;
import com.study.demo.repository.ConfigurationRepository;
import com.study.demo.repository.TransactionRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    TransactionRepository transactionRepo;

    @Mock
    ConfigurationRepository configurationRepo;

    @InjectMocks
    TransactionService transactionService;

    @BeforeEach
    public void setup() {
        lenient().when(configurationRepo.findByName("MAX_AMOUNT")).thenReturn("500");
    }

    @Test
    public void shouldSaveTheTransaction_whenTransactionIsSuccessful() {
        Transaction transaction = TransactionFixtures.simpleTransaction();
        when(transactionRepo.findByTransactionReferenceAndBankCode(transaction.getTransactionReference(),
                transaction.getBankCode())).thenReturn(null);
        transactionService.transact(transaction);
        verify(transactionRepo).save(transaction);
    }

    @Test
    public void shouldReturnException_whenTransactionAmountIsHigherThanLimit() {
        Transaction transaction = TransactionFixtures.transactionWithAmountHigherThanLimit();
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            transactionService.transact(transaction);
        });
    }

    @Test
    public void shouldReturnException_whenTransactionAmountIsZero() {
        Transaction transaction = TransactionFixtures.transactionWithAmountZero();
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            transactionService.transact(transaction);
        });
    }

    @Test
    public void shouldReturnException_whenTransactionReferenceAlreadyExistsForTheGivenBank() {
        Transaction transaction = TransactionFixtures.simpleTransaction();
        when(transactionRepo.findByTransactionReferenceAndBankCode(transaction.getTransactionReference(),
                transaction.getBankCode())).thenReturn(transaction);
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            transactionService.transact(transaction);
        });
    }

    @Test
    public void shouldReturnException_whenPreviousTransactionReferenceHasATransactionThatDoesNotExist() {
        Transaction transaction = TransactionFixtures.transactionWithReferenceToPrevious();

        lenient().when(transactionRepo.findByTransactionReferenceAndBankCode(transaction.getTransactionReference(),
                transaction.getBankCode())).thenReturn(null);
        lenient()
                .when(transactionRepo.findByTransactionReferenceAndBankCode(
                        transaction.getPreviousTransactionReference(), transaction.getReceiverAccount().getBankCode()))
                .thenReturn(null);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            transactionService.transact(transaction);
        });
    }

    @Test
    public void shouldReturnException_whenTransactionIsFromAccountsOfTheSameBank() {
        Transaction transaction = TransactionFixtures.transactionWithAccountsFromTheSameBank();

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            transactionService.transact(transaction);
        });
    }

    @Test
    public void shouldReturnException_whenBankCodeOfTransactionDiffersFromBankCodeOfTheSenderAccount() {
        Transaction transaction = TransactionFixtures.transactionWhoseBankCodeDiffersFromBankCodeOfSenderAccount();

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            transactionService.transact(transaction);
        });
    }

}