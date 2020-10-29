package com.study.demo.dto;

import java.util.List;

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
public class TransactionQueryResponses {

    private List<TransactionQueryResponse> transactions;

    public TransactionQueryResponses ammendTransactions(TransactionQueryResponses transactionsToAmmend) {
        transactions.addAll(transactionsToAmmend.getTransactions());
        return this;
    }
}