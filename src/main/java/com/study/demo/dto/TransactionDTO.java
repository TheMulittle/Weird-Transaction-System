package com.study.demo.dto;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Builder;
import lombok.Data;

@Data
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

    @JsonIgnore
    private String bankCode;
}