package com.study.demo.dto;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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