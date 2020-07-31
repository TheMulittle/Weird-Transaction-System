package com.study.demo.dto;

import javax.validation.constraints.NotEmpty;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TransactionDTO {

    @NotEmpty
    private long amount;

    @NotEmpty
    private String transactionReference;

    private String previousTransactionReference;

    @NotEmpty
    private AccountDTO senderAccount;

    @NotEmpty
    private AccountDTO receiverAccount;
}