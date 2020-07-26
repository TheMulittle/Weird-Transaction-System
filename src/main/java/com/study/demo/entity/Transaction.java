package com.study.demo.entity;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

import com.study.demo.model.User;

import lombok.Data;

@Data
@Entity
public class Transaction {

    @Id
    @GeneratedValue
    private Long id;

    @NotEmpty
    private double amount;

    @NotEmpty
    private String transactionReference;

    private String previousTransactionReference;

    @Embedded
    @NotEmpty
    private User creditorAccount;

    @Embedded
    @NotEmpty
    private User debtorAccount;
}