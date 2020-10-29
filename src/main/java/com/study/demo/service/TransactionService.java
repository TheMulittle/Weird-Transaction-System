package com.study.demo.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.study.demo.dto.TransactionDTO;
import com.study.demo.dto.TransactionQueryResponse;
import com.study.demo.dto.TransactionQueryResponses;
import com.study.demo.entity.Transaction;
import com.study.demo.exception.AmountGreaterThanMaximumException;
import com.study.demo.exception.AmountSmallerThanMinimumException;
import com.study.demo.exception.DuplicatedTransactionException;
import com.study.demo.exception.SameBankException;
import com.study.demo.exception.TransactionNotFoundException;
import com.study.demo.model.DirectionEnum;
import com.study.demo.model.StateEnum;
import com.study.demo.repository.ConfigurationRepository;
import com.study.demo.repository.TransactionRepository;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final ConfigurationRepository configurationRepository;
    private final ModelMapper modelMapper;

    public Transaction transact(TransactionDTO transaction) {

        validateTransactionAmount(transaction);
        validateTransactionIsNotDuplicated(transaction);
        validatePreviousTransactionExists(transaction);
        validateCreditorAndDebtor(transaction);

        Transaction transactionToPersist = modelMapper.map(transaction, Transaction.class);
        transactionToPersist.setState(StateEnum.INITIATED);
        transactionToPersist.setInformed(0);

        return transactionRepository.save(transactionToPersist);
    }

    private void validateTransactionAmount(TransactionDTO transaction) {

        Long maxAmount = Long.valueOf(configurationRepository.findByName("MAX_AMOUNT").getValue());
        Long minAmount = Long.valueOf(configurationRepository.findByName("MIN_AMOUNT").getValue());
        Long amount = transaction.getAmount();

        if (amount <= minAmount) {
            throw new AmountSmallerThanMinimumException(amount, minAmount);
        } else if (amount > maxAmount) {
            throw new AmountGreaterThanMaximumException(amount, maxAmount);
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

	public TransactionQueryResponses retrieveTransactionsByStateAndDiretionAndBankCode(StateEnum state, DirectionEnum direction, String bankCode) {
        List<Transaction> foundTransactions = new ArrayList<Transaction>();

        if(state == null && direction == null) {
            foundTransactions = transactionRepository.findByReceiverBankCode(bankCode);
            foundTransactions.addAll(transactionRepository.findBySenderBankCode(bankCode));
        } else if (direction == null) {
            foundTransactions = transactionRepository.findByReceiverBankCodeAndState(bankCode, state);
            foundTransactions.addAll(transactionRepository.findBySenderBankCodeAndState(bankCode, state));
        } else if (state == null && direction == DirectionEnum.INWARD) {
            foundTransactions = transactionRepository.findByReceiverBankCode(bankCode);
        } else if(state == null && direction == DirectionEnum.OUTWARD) {
            foundTransactions = transactionRepository.findBySenderBankCode(bankCode);
        } else if (direction == DirectionEnum.INWARD) {
            foundTransactions = transactionRepository.findByReceiverBankCodeAndState(bankCode, state);    
        } else if (direction == DirectionEnum.OUTWARD) {
            foundTransactions = transactionRepository.findBySenderBankCodeAndState(bankCode, state);
        }

        foundTransactions.stream().filter(transaction -> transaction.getReceiverBankCode().equals(bankCode)).forEach(transaction -> transaction.setInformed(1));
        transactionRepository.saveAll(foundTransactions);

        TransactionQueryResponse[] transactions = modelMapper.map(foundTransactions, TransactionQueryResponse[].class);

        return new TransactionQueryResponses(Arrays.asList(transactions));
	}

}