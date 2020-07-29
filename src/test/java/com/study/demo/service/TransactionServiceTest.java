package com.study.demo.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.study.demo.dto.TransactionDTO;
import com.study.demo.entity.Transaction;
import com.study.demo.exception.AmountGreaterThanMaximumException;
import com.study.demo.exception.DuplicatedTransactionException;
import com.study.demo.exception.SameBankException;
import com.study.demo.exception.TransactionNotFoundException;
import com.study.demo.exception.ZeroAmountException;
import com.study.demo.fixtures.transaction.TransactionDTOFixtures;
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
        TransactionDTO transaction = TransactionDTOFixtures.simpleTransaction();
        when(transactionRepo.findByTransactionReferenceAndSenderBankCode(transaction.getTransactionReference(),
                transaction.getBankCode())).thenReturn(null);
        transactionService.transact(transaction);
        verify(transactionRepo).save(any());
    }

    @Test
    public void shouldReturnException_whenTransactionAmountIsHigherThanLimit() {
        TransactionDTO transaction = TransactionDTOFixtures.transactionWithAmountHigherThanLimit();
        Assertions.assertThrows(AmountGreaterThanMaximumException.class, () -> {
            transactionService.transact(transaction);
        });
    }

    @Test
    public void shouldReturnException_whenTransactionAmountIsZero() {
        TransactionDTO transaction = TransactionDTOFixtures.transactionWithAmountZero();
        Assertions.assertThrows(ZeroAmountException.class, () -> {
            transactionService.transact(transaction);
        });
    }

    @Test
    public void shouldReturnException_whenTransactionReferenceAlreadyExistsForTheGivenBank() {
        TransactionDTO transactionDTO = TransactionDTOFixtures.simpleTransaction();
        Transaction transactionEntity = TransactionFixtures.simpleTransaction();
        when(transactionRepo.findByTransactionReferenceAndSenderBankCode(transactionDTO.getTransactionReference(),
                transactionDTO.getBankCode())).thenReturn(transactionEntity);
        Assertions.assertThrows(DuplicatedTransactionException.class, () -> {
            transactionService.transact(transactionDTO);
        });
    }

    @Test
    public void shouldReturnException_whenPreviousTransactionReferenceHasATransactionThatDoesNotExist() {
        TransactionDTO transaction = TransactionDTOFixtures.transactionWithReferenceToPrevious();

        lenient()
                .when(transactionRepo.findByTransactionReferenceAndSenderBankCode(transaction.getTransactionReference(),
                        transaction.getBankCode()))
                .thenReturn(null);
        lenient()
                .when(transactionRepo.findByTransactionReferenceAndSenderBankCode(
                        transaction.getPreviousTransactionReference(), transaction.getReceiverAccount().getBankCode()))
                .thenReturn(null);

        Assertions.assertThrows(TransactionNotFoundException.class, () -> {
            transactionService.transact(transaction);
        });
    }

    @Test
    public void shouldReturnException_whenTransactionIsFromAccountsOfTheSameBank() {
        TransactionDTO transaction = TransactionDTOFixtures.transactionWithAccountsFromTheSameBank();

        Assertions.assertThrows(SameBankException.class, () -> {
            transactionService.transact(transaction);
        });
    }

    @Test
    public void shouldReturnException_whenBankCodeOfTransactionDiffersFromBankCodeOfTheSenderAccount() {
        TransactionDTO transaction = TransactionDTOFixtures
                .transactionWhoseBankCodeDiffersFromBankCodeOfSenderAccount();

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            transactionService.transact(transaction);
        });
    }

}