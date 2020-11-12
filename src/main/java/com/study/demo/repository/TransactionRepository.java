package com.study.demo.repository;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.study.demo.entity.Transaction;
import com.study.demo.model.StateEnum;

import org.springframework.data.repository.CrudRepository;

@Transactional
public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    public Transaction findByTransactionReferenceAndSenderEntityCode(String transactionReference, String entityCode);

    public List<Transaction> findByReceiverEntityCode(String entityCode);

    public List<Transaction> findBySenderEntityCode(String entityCode);

    public List<Transaction> findByReceiverEntityCodeAndState(String entityCode, StateEnum state);

    public List<Transaction> findBySenderEntityCodeAndState(String entityCode, StateEnum state);
}