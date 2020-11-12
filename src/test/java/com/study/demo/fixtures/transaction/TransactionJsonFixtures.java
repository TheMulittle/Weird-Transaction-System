package com.study.demo.fixtures.transaction;

import static org.apache.commons.io.FileUtils.readFileToString;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;

public class TransactionJsonFixtures {

    private static final String BASE_PATH = "src/test/resources/transaction";

    public static String simple() throws IOException {

        return readFileToString(new File("src/test/resources/transaction/SimpleTransaction.json"), "UTF-8");
    }

    public static String sameEntityTransaction() throws IOException {

        return readFileToString(new File("src/test/resources/transaction/SameEntityTransaction.json"), "UTF-8");
    }

    public static Stream<Arguments> notBlankValidation() throws IOException {
        return Stream.of(Arguments.of(readJson("missingReference.json"), "transactionReference"),
                Arguments.of(readJson("missingSenderEntityCode.json"), "senderAccount.entityCode"),
                Arguments.of(readJson("missingSenderDocumentNumber.json"), "senderAccount.ownerDocumentNumber"),
                Arguments.of(readJson("missingSenderFirstName.json"), "senderAccount.ownerFirstName"),
                Arguments.of(readJson("emptySenderFirstName.json"), "senderAccount.ownerFirstName"),
                Arguments.of(readJson("emptyReference.json"), "transactionReference"));
    }

    public static Stream<Arguments> notNullValidation() throws IOException {
        return Stream.of(Arguments.of(readJson("missingAmount.json"), "amount"),
                Arguments.of(readJson("missingReceiverAccount.json"), "receiverAccount"));

    }

    public static String emptyPreviousReference() throws IOException {
        return readJson("emptyPreviousReference.json");

    }

    public static String alreadyExistingTransaction() throws IOException {
        return readJson("alreadyExistingTransaction.json");

    }

    public static String notExistingPrevioiusTransaction() throws IOException {
        return readJson("notExistingPreviousTransaction.json");
    }

    public static String existingPreviousTransaction() throws IOException {
        return readJson("existingPreviousTransaction.json");
    }

    public static String readJson(String path) throws IOException {
        return readFileToString(new File(BASE_PATH.concat("/").concat(path)), "UTF-8");
    }
}