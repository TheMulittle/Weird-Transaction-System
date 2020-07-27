package com.study.demo.fixtures.account;

import com.study.demo.entity.Account;

public class AccountFixtures {

    public static Account simpleReceiverAccount() {

        return Account.builder().bankCode("35").accountNumber("123654123").ownerDocumentNumber("WWW123456")
                .ownerFirstName("John").ownerLastName("Johnsons").build();
    }

    public static Account simpleSenderAccount() {

        return Account.builder().bankCode("01").accountNumber("000654123").ownerDocumentNumber("246813579")
                .ownerFirstName("James").ownerLastName("Jameson").build();
    }
}