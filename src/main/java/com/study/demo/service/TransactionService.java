package com.study.demo.service;

import com.study.demo.entity.Transaction;
import com.study.demo.repository.TransactionRepository;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public Transaction transact(Transaction transaction) {

        //

        return transactionRepository.save(transaction);
    }

}