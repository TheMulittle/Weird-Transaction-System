package com.study.demo.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@IdClass(TransactionId.class)
public class Transaction {

    @NotNull
    private long amount;

    @Id
    @NotEmpty
    private String transactionReference;

    private String previousTransactionReference;

    @NotEmpty
    private String receiverFirstName;

    @NotEmpty
    private String receiverLastName;

    @NotEmpty
    private String receiverDocumentNumber;

    @NotEmpty
    private String receiverAccountNumber;

    @Id
    @NotEmpty
    private String receiverBankCode;

    @NotEmpty
    private String senderFirstName;

    @NotEmpty
    private String senderLastName;

    @NotEmpty
    private String senderDocumentNumber;

    @NotEmpty
    private String senderAccountNumber;

    @NotEmpty
    private String senderBankCode;

}