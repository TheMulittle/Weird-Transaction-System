package com.study.demo.repository;

import org.springframework.transaction.annotation.Transactional;

import com.study.demo.entity.Transaction;

import org.springframework.data.repository.CrudRepository;

@Transactional
public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    public Transaction findByTransactionReferenceAndSenderBankCode(String transactionReference, String bankCode);
}