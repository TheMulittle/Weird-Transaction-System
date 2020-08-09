package com.study.demo.integration;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.study.demo.entity.Transaction;
import com.study.demo.fixtures.transaction.TransactionJsonFixtures;
import com.study.demo.repository.TransactionRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class TransactionIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TransactionRepository transactionRepo;

    @ParameterizedTest(name = "When ''{1}'' is blank or null in /transaction/payment/iniate post call, should return 400 BAD REQUEST")
    @MethodSource("com.study.demo.fixtures.transaction.TransactionJsonFixtures#notBlankValidation")
    public void shouldReturn400BadRequest_whenAParameterThatShouldnotBeBlankIsBlank(String transaction, String field)
            throws Exception {

        RequestBuilder request = MockMvcRequestBuilders.post("/transaction/payment/initiate", transaction)
                .header("SIGNATURE", "123456").contentType(MediaType.APPLICATION_JSON).content(transaction);

        mockMvc.perform(request).andExpect(status().isBadRequest()).andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMessage").value("Message body failed validation"))
                .andExpect(jsonPath("$.details").value(containsString(field)))
                .andExpect(jsonPath("$.details").value(containsString("must not be blank")));
    }

    @ParameterizedTest(name = "When ''{1}'' is  null in /transaction/payment/iniate post call, should return 400 BAD REQUEST")
    @MethodSource("com.study.demo.fixtures.transaction.TransactionJsonFixtures#notNullValidation")
    public void shouldReturn400BadRequest_whenARequiredParameterIsBlank(String transaction, String field)
            throws Exception {

        RequestBuilder request = MockMvcRequestBuilders.post("/transaction/payment/initiate", transaction)
                .header("SIGNATURE", "123456").contentType(MediaType.APPLICATION_JSON).content(transaction);

        mockMvc.perform(request).andExpect(status().isBadRequest()).andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMessage").value("Message body failed validation"))
                .andExpect(jsonPath("$.details").value(containsString(field)))
                .andExpect(jsonPath("$.details").value(containsString("must not be null")));
    }

    @Test
    @DisplayName("When 'previousTransaction' is empty in /transaction/payment/iniate post call, should return 400 BAD REQUEST")
    public void shouldReturn400BadRequest_whenANonRequiredParameterIsEmpty() throws Exception {

        String transaction = TransactionJsonFixtures.emptyPreviousReference();

        RequestBuilder request = MockMvcRequestBuilders.post("/transaction/payment/initiate", transaction)
                .header("SIGNATURE", "123456").contentType(MediaType.APPLICATION_JSON).content(transaction);

        mockMvc.perform(request).andExpect(status().isBadRequest()).andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMessage").value("Message body failed validation"))
                .andExpect(jsonPath("$.details").value(containsString("previousTransactionReference")))
                .andExpect(jsonPath("$.details").value(containsString("must not be empty")));
    }

    @Test
    @DisplayName("When the value of 'transactionReference' matches an already existing transaction in /transaction/payment/iniate post call, should return 400 BAD REQUEST")
    public void shouldReturn400BadRequest_whenTryingToPerfomAnAlreadyExistingTransaction_() throws Exception {
        String transactionJson = TransactionJsonFixtures.alreadyExistingTransaction();

        RequestBuilder request = MockMvcRequestBuilders.post("/transaction/payment/initiate", transactionJson)
                .header("SIGNATURE", "123456").contentType(MediaType.APPLICATION_JSON).content(transactionJson);

        mockMvc.perform(request).andExpect(status().isBadRequest()).andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMessage").value("Duplicated transaction"))
                .andExpect(jsonPath("$.details").value(containsString("{01}")))
                .andExpect(jsonPath("$.details").value(containsString("{9999999999}")));
    }

    @Test
    @DisplayName("When the value of 'previousTransactionReference' does not match a previous transasction in /transaction/payment/iniate post call, should return 400 BAD REQUEST")
    public void shouldReturn400BadRequest_whenThePreviousTransactionReferenceDoesNotMatchAValidTransaction_()
            throws Exception {

        String transactionJson = TransactionJsonFixtures.notExistingPrevioiusTransaction();

        RequestBuilder request = MockMvcRequestBuilders.post("/transaction/payment/initiate", transactionJson)
                .header("SIGNATURE", "123456").contentType(MediaType.APPLICATION_JSON).content(transactionJson);

        mockMvc.perform(request).andExpect(status().isBadRequest()).andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMessage").value("Transaction not found"))
                .andExpect(jsonPath("$.details").value(containsString("{35}")))
                .andExpect(jsonPath("$.details").value(containsString("{3213213211}")));
    }

    @Test
    @DisplayName("When all fields pass validation in /transaction/payment/iniate post call, should return 201 CREATED and persist to the database")
    public void shouldPersitTheTransaction_whenAllFieldsPassValidation() throws Exception {

        String transactionJson = TransactionJsonFixtures.simple();

        RequestBuilder request = MockMvcRequestBuilders.post("/transaction/payment/initiate", transactionJson)
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
    }

    @Test
    @DisplayName("When a transaction refers to a previous transaction via 'previousTransactionReference' and that transaction exists, when post calling /transaction/payment/iniate, should return 201 CREATED and persist to the database")
    public void shouldPersitTheTransaction_whenTheTransactionReferencedByPreviousTransactionReferenceExists()
            throws Exception {

        String transactionJson = TransactionJsonFixtures.existingPreviousTransaction();

        RequestBuilder request = MockMvcRequestBuilders.post("/transaction/payment/initiate", transactionJson)
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

}
