package com.study.demo.repository;

import org.springframework.transaction.annotation.Transactional;

import com.study.demo.entity.BankLink;

import org.springframework.data.repository.CrudRepository;

@Transactional
public interface BankLinkRepository extends CrudRepository<BankLink, Long> {
    public BankLink findByBankCodeAndBankIP(String bankCode, String bankIP);

    public BankLink findByBankIP(String bankIP);
}