package com.study.demo.service;

import com.study.demo.entity.Transaction;
import com.study.demo.repository.ConfigurationRepository;
import com.study.demo.repository.TransactionRepository;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

//TODO create specific Exception model for the application
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final ConfigurationRepository configurationRepository;

    public Transaction transact(Transaction transaction) {

        validateBankCode(transaction);
        validateTransactionAmount(transaction);
        validateTransactionIsNotDuplicated(transaction);
        validatePreviousTransactionExists(transaction);
        validateCreditorAndDebtor(transaction);

        return transactionRepository.save(transaction);
    }

    private void validateBankCode(Transaction transaction) {
        if (!transaction.getBankCode().equals(transaction.getSenderAccount().getBankCode())) {
            throw new IllegalArgumentException(
                    "The transaction bank code differs from the bank code of the sender account");
        }
    }

    private void validateTransactionAmount(Transaction transaction) {

        Long maxAmount = Long.valueOf(configurationRepository.findByName("MAX_AMOUNT"));

        if (transaction.getAmount() == 0L) {
            throw new IllegalArgumentException("The transaction amount cannot be zero");
        } else if (transaction.getAmount() > maxAmount) {
            throw new IllegalArgumentException("The transaction amount cannot be bigger than " + maxAmount);
        }
    }

    private void validateTransactionIsNotDuplicated(Transaction transaction) {
        Transaction searchResult = transactionRepository.findByTransactionReferenceAndBankCode(
                transaction.getTransactionReference(), transaction.getBankCode());

        if (searchResult != null) {
            throw new IllegalArgumentException(
                    "A transaction with reference " + transaction.getTransactionReference() + " already exists");
        }
    }

    private void validatePreviousTransactionExists(Transaction transaction) {

        if (transaction.getPreviousTransactionReference() != null
                && !transaction.getPreviousTransactionReference().isEmpty()) {
            Transaction searchResult = transactionRepository.findByTransactionReferenceAndBankCode(
                    transaction.getPreviousTransactionReference(), transaction.getReceiverAccount().getBankCode());

            if (searchResult == null) {
                throw new IllegalArgumentException("The previous transaction with the given reference was not found:  "
                        + transaction.getPreviousTransactionReference());
            }
        }
    }

    private void validateCreditorAndDebtor(Transaction transaction) {
        if (transaction.getSenderAccount().getBankCode().equals(transaction.getReceiverAccount().getBankCode())) {
            throw new IllegalArgumentException("Both creditor and debtor accounts pertain to the same bank");
        }
    }

}