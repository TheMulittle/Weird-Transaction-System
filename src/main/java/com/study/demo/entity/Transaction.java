package com.study.demo.entity;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

import lombok.Builder;
import lombok.Data;

//TODO disjoint what is DAO and what is DTO
@Data
@Builder
@Entity
public class Transaction {

    @Id
    @GeneratedValue
    private Long id;

    @NotEmpty
    private long amount;

    @NotEmpty
    private String transactionReference;

    private String previousTransactionReference;

    @Embedded
    @NotEmpty
    private Account senderAccount;

    @Embedded
    @NotEmpty
    private Account receiverAccount;

    // This will pertain to the DTO only
    private String bankCode;
}