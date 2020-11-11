package com.study.demo.integration;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.study.demo.entity.Transaction;
import com.study.demo.fixtures.transaction.TransactionJsonFixtures;
import com.study.demo.model.StateEnum;
import com.study.demo.repository.TransactionRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

class TransactionIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private TransactionRepository transactionRepo;

    private final static String PAYMENT_ENDPOINT = "/transaction/payment";

    @ParameterizedTest(name = "When ''{1}'' is blank or null in /transaction/payment post call, should return 400 BAD REQUEST")
    @MethodSource("com.study.demo.fixtures.transaction.TransactionJsonFixtures#notBlankValidation")
    public void shouldReturn400BadRequest_whenAParameterThatShouldnotBeBlankIsBlank(String transaction, String field)
            throws Exception {

        RequestBuilder request = MockMvcRequestBuilders.post(PAYMENT_ENDPOINT, transaction)
                .header("SIGNATURE", "123456").contentType(MediaType.APPLICATION_JSON).content(transaction);

        mockMvc.perform(request).andExpect(status().isBadRequest()).andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMessage").value("Message body failed validation"))
                .andExpect(jsonPath("$.details").value(containsString(field)))
                .andExpect(jsonPath("$.details").value(containsString("must not be blank")));
    }

    @ParameterizedTest(name = "When ''{1}'' is  null in /transaction/payment post call, should return 400 BAD REQUEST")
    @MethodSource("com.study.demo.fixtures.transaction.TransactionJsonFixtures#notNullValidation")
    public void shouldReturn400BadRequest_whenARequiredParameterIsBlank(String transaction, String field)
            throws Exception {

        RequestBuilder request = MockMvcRequestBuilders.post(PAYMENT_ENDPOINT, transaction)
                .header("SIGNATURE", "123456").contentType(MediaType.APPLICATION_JSON).content(transaction);

        mockMvc.perform(request).andExpect(status().isBadRequest()).andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMessage").value("Message body failed validation"))
                .andExpect(jsonPath("$.details").value(containsString(field)))
                .andExpect(jsonPath("$.details").value(containsString("must not be null")));
    }

    @Test
    @DisplayName("When 'previousTransaction' is empty in /transaction/payment post call, should return 400 BAD REQUEST")
    public void shouldReturn400BadRequest_whenANonRequiredParameterIsEmpty() throws Exception {

        String transaction = TransactionJsonFixtures.emptyPreviousReference();

        RequestBuilder request = MockMvcRequestBuilders.post(PAYMENT_ENDPOINT, transaction)
                .header("SIGNATURE", "123456").contentType(MediaType.APPLICATION_JSON).content(transaction);

        mockMvc.perform(request).andExpect(status().isBadRequest()).andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMessage").value("Message body failed validation"))
                .andExpect(jsonPath("$.details").value(containsString("previousTransactionReference")))
                .andExpect(jsonPath("$.details").value(containsString("must not be empty")));
    }

    @Test
    @DisplayName("When the value of 'transactionReference' matches an already existing transaction in /transaction/payment post call, should return 400 BAD REQUEST")
    public void shouldReturn400BadRequest_whenTryingToPerfomAnAlreadyExistingTransaction_() throws Exception {
        String transactionJson = TransactionJsonFixtures.alreadyExistingTransaction();

        RequestBuilder request = MockMvcRequestBuilders.post(PAYMENT_ENDPOINT, transactionJson)
                .header("SIGNATURE", "123456").contentType(MediaType.APPLICATION_JSON).content(transactionJson);

        mockMvc.perform(request).andExpect(status().isBadRequest()).andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMessage").value("Duplicated transaction"))
                .andExpect(jsonPath("$.details").value(containsString("{01}")))
                .andExpect(jsonPath("$.details").value(containsString("{9999999999}")));
    }

    @Test
    @DisplayName("When the value of 'previousTransactionReference' does not match a previous transasction in /transaction/payment post call, should return 400 BAD REQUEST")
    public void shouldReturn400BadRequest_whenThePreviousTransactionReferenceDoesNotMatchAValidTransaction_()
            throws Exception {

        String transactionJson = TransactionJsonFixtures.notExistingPrevioiusTransaction();

        RequestBuilder request = MockMvcRequestBuilders.post(PAYMENT_ENDPOINT, transactionJson)
                .header("SIGNATURE", "123456").contentType(MediaType.APPLICATION_JSON).content(transactionJson);

        mockMvc.perform(request).andExpect(status().isBadRequest()).andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMessage").value("Transaction not found"))
                .andExpect(jsonPath("$.details").value(containsString("{35}")))
                .andExpect(jsonPath("$.details").value(containsString("{3213213211}")));
    }

    @Test
    @DisplayName("When all fields pass validation in /transaction/payment post call, should return 201 CREATED and persist to the database")
    public void shouldPersitTheTransaction_whenAllFieldsPassValidation() throws Exception {

        String transactionJson = TransactionJsonFixtures.simple();

        RequestBuilder request = MockMvcRequestBuilders.post(PAYMENT_ENDPOINT, transactionJson)
                .header("SIGNATURE", "123456").contentType(MediaType.APPLICATION_JSON).content(transactionJson);

        mockMvc.perform(request).andExpect(status().isCreated());

        Transaction persistedTransction = transactionRepo.findByTransactionReferenceAndSenderBankCode("000123321",
                "01");

        assertThat(persistedTransction, notNullValue());
        assertThat(persistedTransction.getAmount(), is(50L));
        assertThat(persistedTransction.getTransactionReference(), is("000123321"));
        assertThat(persistedTransction.getSenderFirstName(), is("James"));
        assertThat(persistedTransction.getSenderLastName(), is("Jameson"));
        assertThat(persistedTransction.getSenderDocumentNumber(), is("246813579"));
        assertThat(persistedTransction.getSenderAccountNumber(), is("000654123"));
        assertThat(persistedTransction.getSenderBankCode(), is("01"));
        assertThat(persistedTransction.getReceiverFirstName(), is("John"));
        assertThat(persistedTransction.getReceiverLastName(), is("Johnsons"));
        assertThat(persistedTransction.getReceiverDocumentNumber(), is("WWW123456"));
        assertThat(persistedTransction.getReceiverAccountNumber(), is("123654123"));
        assertThat(persistedTransction.getReceiverBankCode(), is("35"));
        assertThat(persistedTransction.getState(), is(StateEnum.INITIATED));
    }

    @Test
    @DisplayName("When a transaction refers to a previous transaction via 'previousTransactionReference' and that transaction exists, when post calling /transaction/payment, should return 201 CREATED and persist to the database")
    public void shouldPersitTheTransaction_whenTheTransactionReferencedByPreviousTransactionReferenceExists()
            throws Exception {

        String transactionJson = TransactionJsonFixtures.existingPreviousTransaction();

        RequestBuilder request = MockMvcRequestBuilders.post(PAYMENT_ENDPOINT, transactionJson)
                .header("SIGNATURE", "123456").contentType(MediaType.APPLICATION_JSON).content(transactionJson);

        mockMvc.perform(request).andExpect(status().isCreated());

        Transaction persistedTransction = transactionRepo.findByTransactionReferenceAndSenderBankCode("1111111111",
                "01");

        assertThat(persistedTransction, notNullValue());
        assertThat(persistedTransction.getAmount(), is(50L));
        assertThat(persistedTransction.getTransactionReference(), is("1111111111"));
        assertThat(persistedTransction.getSenderFirstName(), is("Receiver"));
        assertThat(persistedTransction.getSenderLastName(), is("Receiverson"));
        assertThat(persistedTransction.getSenderDocumentNumber(), is("WWW123456"));
        assertThat(persistedTransction.getSenderAccountNumber(), is("123654123"));
        assertThat(persistedTransction.getSenderBankCode(), is("01"));
        assertThat(persistedTransction.getReceiverFirstName(), is("Sender"));
        assertThat(persistedTransction.getReceiverLastName(), is("Senderson"));
        assertThat(persistedTransction.getReceiverDocumentNumber(), is("246813579"));
        assertThat(persistedTransction.getReceiverAccountNumber(), is("000654123"));
        assertThat(persistedTransction.getReceiverBankCode(), is("35"));
    }

    @Test
    @DisplayName("When a bank makes GET call to /transaction/payment?state=INITIATED?direction=INWARD, it should receive all pending payments towards it")
    public void shouldReturnAListOfTransactions_whenFilterByInitiatedState() throws Exception {

        // GIVEN there are transactions initiated by bank A towards bank B

        RequestBuilder requestForInitiated = MockMvcRequestBuilders.get(PAYMENT_ENDPOINT).header("SIGNATURE", "123456")
                .queryParam("state", "INITIATED").queryParam("direction", "INWARD")
                .contentType(MediaType.APPLICATION_JSON);

        // WHEN bank B queries for the inward initiated payments
        // THEN should return a list of inward pending payments
        mockMvc.perform(requestForInitiated).andExpect(status().isOk()).andExpect(jsonPath("$.transactions").isArray())
                .andExpect(jsonPath("$.transactions", hasSize(1)))
                .andExpect(jsonPath("$.transactions[0].transactionReference").value(is("0000000000")));

        // THEN should mark the transaction as informed in the DB
        Transaction actualTransaction = transactionRepo.findByTransactionReferenceAndSenderBankCode("0000000000", "35");
        assertThat(actualTransaction.getInformed(), is(1));
    }

    @Test
    @DisplayName("When a bank makes GET call to /transaction/payment?status=CONFIRMED and there are isn´t any pending payment, it should receive an empty list")
    public void shouldReturnAnEmptyList_whenFilterByStateAndThereIsNotAnyPaymentInThatState() throws Exception {

        // GIVEN there are no transactions initiated by bank A towards bank B
        RequestBuilder requestForInitiated = MockMvcRequestBuilders.get(PAYMENT_ENDPOINT).header("SIGNATURE", "123456")
                .queryParam("state", "CONFIRMED").queryParam("direction", "OUTWARD")
                .contentType(MediaType.APPLICATION_JSON);

        // WHEN bank B queries for the initiated payments towards it
        // THEN should return an empty list
        mockMvc.perform(requestForInitiated).andExpect(status().isOk()).andExpect(jsonPath("$.transactions").isArray())
                .andExpect(jsonPath("$.transactions", hasSize(0)));
    }

    @Test
    @DisplayName("When a bank makes GET call to /transaction/payment?state=XX and XX is not a valid state, the bank should receive a 400 Bad Request")
    public void shouldReturn400BadRequest_whenFilterByAnIvalidState() throws Exception {

        RequestBuilder requestForInitiated = MockMvcRequestBuilders.get(PAYMENT_ENDPOINT).header("SIGNATURE", "123456")
                .queryParam("state", "XX").queryParam("direction", "OUTWARD").contentType(MediaType.APPLICATION_JSON);

        // WHEN bank B queries for an invalid state
        // THEN should return a 400 Bad Request status
        mockMvc.perform(requestForInitiated).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMessage").value("Value not valid"));
    }

    @Test
    @DisplayName("When a bank makes GET call to /transaction/payment?direction=INWARD (without a state), all transactions in that direction should be returned")
    public void shouldReturnAListOfTransactions_whenFilterOnlyByDirection() throws Exception {
        // GIVEN there are no transactions initiated by bank A towards bank B

        // WHEN bank B queries for transactions specifing only the direction, but not
        // the state
        RequestBuilder requestForInitiated = MockMvcRequestBuilders.get(PAYMENT_ENDPOINT).header("SIGNATURE", "123456")
                .queryParam("direction", "INWARD").contentType(MediaType.APPLICATION_JSON);
        // THEN should return a list of payments with that direction
        mockMvc.perform(requestForInitiated).andExpect(status().isOk()).andExpect(jsonPath("$.transactions").isArray())
                .andExpect(jsonPath("$.transactions", hasSize(2)))
                .andExpect(jsonPath("$.transactions[0].transactionReference").value(is("0000000000")));

        // THEN should mark the transaction as informed in the DB
        Transaction actualTransaction = transactionRepo.findByTransactionReferenceAndSenderBankCode("0000000000", "35");
        assertThat(actualTransaction.getInformed(), is(1));
    }

}
