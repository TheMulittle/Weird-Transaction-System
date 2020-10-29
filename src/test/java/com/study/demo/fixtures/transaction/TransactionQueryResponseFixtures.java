package com.study.demo.fixtures.transaction;

import java.util.Arrays;

import com.study.demo.dto.TransactionQueryResponse;
import com.study.demo.dto.TransactionQueryResponses;
import com.study.demo.fixtures.account.AccountDTOFixtures;
import com.study.demo.model.StateEnum;

public class TransactionQueryResponseFixtures {

    public static TransactionQueryResponses simpleQueryResponses() {

        TransactionQueryResponse simpleQueryResponse = simpleQueryResponse();

        return TransactionQueryResponses.builder().transactions(Arrays.asList(simpleQueryResponse)).build();
    }

    public static TransactionQueryResponse simpleQueryResponse() {

        return TransactionQueryResponse.builder()
                .transactionReference("01234567").amount(50L).previousTransactionReference(null)
                .receiverAccount(AccountDTOFixtures.simpleReceiverAccount())
                .senderAccount(AccountDTOFixtures.simpleSenderAccount()).status(StateEnum.INITIATED).build();
       
    }

}