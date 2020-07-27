package com.study.demo.fixtures.transaction;

import com.study.demo.entity.Transaction;
import com.study.demo.fixtures.account.AccountFixtures;

public class TransactionFixtures {

    public static Transaction simpleTransaction() {

        return Transaction.builder().amount(50L).bankCode("01").transactionReference("000123321").id(5L)
                .receiverAccount(AccountFixtures.simpleReceiverAccount())
                .senderAccount(AccountFixtures.simpleSenderAccount()).build();
    }

    public static Transaction transactionMissingAmount() {

        return Transaction.builder().bankCode("01").transactionReference("000123321").id(5L)
                .receiverAccount(AccountFixtures.simpleReceiverAccount())
                .senderAccount(AccountFixtures.simpleSenderAccount()).build();
    }

    public static Transaction transactionWithAmountHigherThanLimit() {
        return Transaction.builder().bankCode("01").amount(50000L).transactionReference("000123321").id(5L)
                .receiverAccount(AccountFixtures.simpleReceiverAccount())
                .senderAccount(AccountFixtures.simpleSenderAccount()).build();
    }

    public static Transaction transactionWithAmountZero() {
        return Transaction.builder().bankCode("01").amount(0L).transactionReference("000123321").id(5L)
                .receiverAccount(AccountFixtures.simpleReceiverAccount())
                .senderAccount(AccountFixtures.simpleSenderAccount()).build();
    }

    public static Transaction transactionWithReferenceToPrevious() {
        return Transaction.builder().bankCode("01").amount(50L).transactionReference("000123321")
                .previousTransactionReference("5555555").id(5L).receiverAccount(AccountFixtures.simpleReceiverAccount())
                .senderAccount(AccountFixtures.simpleSenderAccount()).build();
    }

    public static Transaction transactionWithAccountsFromTheSameBank() {

        return Transaction.builder().bankCode("01").amount(50L).transactionReference("000123321").id(5L)
                .receiverAccount(AccountFixtures.simpleSenderAccount())
                .senderAccount(AccountFixtures.simpleSenderAccount()).build();
    }

    public static Transaction transactionWhoseBankCodeDiffersFromBankCodeOfSenderAccount() {

        return Transaction.builder().bankCode("99").amount(50L).transactionReference("000123321").id(5L)
                .receiverAccount(AccountFixtures.simpleReceiverAccount())
                .senderAccount(AccountFixtures.simpleSenderAccount()).build();
    }
}