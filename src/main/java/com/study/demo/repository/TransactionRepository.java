package com.study.demo.repository;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.study.demo.entity.Transaction;
import com.study.demo.model.StateEnum;

import org.springframework.data.repository.CrudRepository;

@Transactional
public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    public Transaction findByTransactionReferenceAndSenderBankCode(String transactionReference, String bankCode);

    public List<Transaction> findByReceiverBankCode(String bankCode);
    
    public List<Transaction> findBySenderBankCode(String bankCode);

    public List<Transaction> findByReceiverBankCodeAndState(String bankCode, StateEnum state);
    
    public List<Transaction> findBySenderBankCodeAndState(String bankCode, StateEnum state);
}