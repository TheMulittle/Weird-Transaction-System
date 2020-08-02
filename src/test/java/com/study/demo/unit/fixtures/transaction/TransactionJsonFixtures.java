package com.study.demo.unit.fixtures.transaction;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class TransactionJsonFixtures {

    public static String simple() throws IOException {

        return FileUtils.readFileToString(new File("src/test/resources/transaction/SimpleTransaction.json"), "UTF-8");
    }
}