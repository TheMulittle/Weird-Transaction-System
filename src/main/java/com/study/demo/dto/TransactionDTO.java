package com.study.demo.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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

    @NotNull
    private long amount;

    @NotBlank
    private String transactionReference;

    private String previousTransactionReference;

    @NotNull
    private AccountDTO senderAccount;

    @NotNull
    private AccountDTO receiverAccount;
}