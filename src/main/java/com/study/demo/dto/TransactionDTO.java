package com.study.demo.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.study.demo.validator.NullOrNotBlank;

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
    private Long amount;

    @NotBlank
    private String transactionReference;

    @NullOrNotBlank
    private String previousTransactionReference;

    @Valid
    @NotNull
    private AccountDTO senderAccount;

    @Valid
    @NotNull
    private AccountDTO receiverAccount;
}