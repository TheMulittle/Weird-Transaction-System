package com.study.demo.unit.fixtures.transaction;

import static org.apache.commons.io.FileUtils.readFileToString;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

public class TransactionJsonFixtures {

    public static String simple() throws IOException {

        return readFileToString(new File("src/test/resources/transaction/SimpleTransaction.json"), "UTF-8");
    }

    public static String sameBankTransaction() throws IOException {

        return readFileToString(new File("src/test/resources/transaction/SameBankTransaction.json"), "UTF-8");
    }

    public static Stream<String> missingParameters() throws IOException {
        return Stream.of(readFileToString(new File("src/test/resources/transaction/emptyReference.json"), "UTF-8"),
                readFileToString(new File("src/test/resources/transaction/missingAmount.json"), "UTF-8"),
                readFileToString(new File("src/test/resources/transaction/missingReceiverAccount.json"), "UTF-8"),
                readFileToString(new File("src/test/resources/transaction/missingReference.json"), "UTF-8"),
                readFileToString(new File("src/test/resources/transaction/missingSenderAccountNumber.json"), "UTF-8"),
                readFileToString(new File("src/test/resources/transaction/missingSenderBankCode.json"), "UTF-8"),
                readFileToString(new File("src/test/resources/transaction/missingSenderDocumentNumber.json"), "UTF-8"),
                readFileToString(new File("src/test/resources/transaction/missingSenderFirstName.json"), "UTF-8"));

    }
}