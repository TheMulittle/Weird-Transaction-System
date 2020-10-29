package com.study.demo.dto;

import com.study.demo.model.StateEnum;

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
public class TransactionQueryResponse {

    private Long amount;

    private String transactionReference;

    private String previousTransactionReference;

    private AccountDTO senderAccount;

    private AccountDTO receiverAccount;

    private StateEnum status;
}