package com.study.demo.service;

import com.study.demo.dto.TransactionDTO;
import com.study.demo.entity.Transaction;
import com.study.demo.exception.AmountGreaterThanMaximumException;
import com.study.demo.exception.DuplicatedTransactionException;
import com.study.demo.exception.SameBankException;
import com.study.demo.exception.TransactionNotFoundException;
import com.study.demo.exception.ZeroAmountException;
import com.study.demo.repository.ConfigurationRepository;
import com.study.demo.repository.TransactionRepository;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

//TODO create specific Exception model for the application
@Service
@RequiredArgsConstructor
public class TransactionService {

    ModelMapper modelMapper = new ModelMapper();

    private final TransactionRepository transactionRepository;
    private final ConfigurationRepository configurationRepository;

    public Transaction transact(TransactionDTO transaction) {

        validateTransactionAmount(transaction);
        validateTransactionIsNotDuplicated(transaction);
        validatePreviousTransactionExists(transaction);
        validateCreditorAndDebtor(transaction);

        Transaction transactionToPersist = modelMapper.map(transaction, Transaction.class);

        return transactionRepository.save(transactionToPersist);
    }

    private void validateTransactionAmount(TransactionDTO transaction) {

        Long maxAmount = Long.valueOf(configurationRepository.findByName("MAX_AMOUNT"));

        if (transaction.getAmount() == 0L) {
            throw new ZeroAmountException();
        } else if (transaction.getAmount() > maxAmount) {
            throw new AmountGreaterThanMaximumException(transaction.getAmount(), maxAmount);
        }
    }

    private void validateTransactionIsNotDuplicated(TransactionDTO transaction) {
        Transaction searchResult = transactionRepository.findByTransactionReferenceAndSenderBankCode(
                transaction.getTransactionReference(), transaction.getSenderAccount().getBankCode());

        if (searchResult != null) {
            throw new DuplicatedTransactionException(transaction.getTransactionReference(),
                    transaction.getSenderAccount().getBankCode());
        }
    }

    private void validatePreviousTransactionExists(TransactionDTO transaction) {

        if (transaction.getPreviousTransactionReference() != null
                && !transaction.getPreviousTransactionReference().isEmpty()) {
            Transaction searchResult = transactionRepository.findByTransactionReferenceAndSenderBankCode(
                    transaction.getPreviousTransactionReference(), transaction.getReceiverAccount().getBankCode());

            if (searchResult == null) {
                throw new TransactionNotFoundException(transaction.getPreviousTransactionReference(),
                        transaction.getReceiverAccount().getBankCode());
            }
        }
    }

    private void validateCreditorAndDebtor(TransactionDTO transaction) {
        if (transaction.getSenderAccount().getBankCode().equals(transaction.getReceiverAccount().getBankCode())) {
            throw new SameBankException(transaction.getReceiverAccount().getBankCode());
        }
    }

}