package com.study.demo.fixtures.transaction;

import com.study.demo.entity.Transaction;
//import com.study.demo.fixtures.account.AccountFixtures;

public class TransactionFixtures {

    public static Transaction simpleTransaction() {

        return Transaction.builder().amount(50L).transactionReference("000123321").receiverEntityCode("35")
                .receiverAccountNumber("123654123").receiverDocumentNumber("WWW123456").receiverFirstName("John")
                .receiverLastName("Johnsons").senderEntityCode("01").senderAccountNumber("000654123")
                .senderDocumentNumber("246813579").senderFirstName("James").senderLastName("Jameson").build();
    }
}