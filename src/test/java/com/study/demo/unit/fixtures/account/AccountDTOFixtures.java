package com.study.demo.unit.fixtures.account;

import com.study.demo.dto.AccountDTO;

public class AccountDTOFixtures {

    public static AccountDTO simpleReceiverAccount() {

        return AccountDTO.builder().bankCode("35").accountNumber("123654123").ownerDocumentNumber("WWW123456")
                .ownerFirstName("John").ownerLastName("Johnsons").build();
    }

    public static AccountDTO simpleSenderAccount() {

        return AccountDTO.builder().bankCode("01").accountNumber("000654123").ownerDocumentNumber("246813579")
                .ownerFirstName("James").ownerLastName("Jameson").build();
    }
}