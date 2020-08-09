package com.study.demo.integration;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.study.demo.entity.Transaction;
import com.study.demo.fixtures.transaction.TransactionJsonFixtures;
import com.study.demo.repository.TransactionRepository;

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

    @ParameterizedTest
    @MethodSource("com.study.demo.fixtures.transaction.TransactionJsonFixtures#notBlankValidation")
    public void whenAParameterThatShouldnotBeBlankIsBlank_ShouldReturn400BadRequest(String transaction, String field)
            throws Exception {

        RequestBuilder request = MockMvcRequestBuilders.post("/transaction/payment/initiate", transaction)
                .header("SIGNATURE", "123456").contentType(MediaType.APPLICATION_JSON).content(transaction);

        mockMvc.perform(request).andExpect(status().isBadRequest()).andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMessage").value("Message body failed validation"))
                .andExpect(jsonPath("$.details").value(containsString(field)))
                .andExpect(jsonPath("$.details").value(containsString("must not be blank")));
    }

    @ParameterizedTest
    @MethodSource("com.study.demo.fixtures.transaction.TransactionJsonFixtures#notNullValidation")
    public void whenARequiredParameterIsBlank_ShouldReturn400BadRequest(String transaction, String field)
            throws Exception {

        RequestBuilder request = MockMvcRequestBuilders.post("/transaction/payment/initiate", transaction)
                .header("SIGNATURE", "123456").contentType(MediaType.APPLICATION_JSON).content(transaction);

        mockMvc.perform(request).andExpect(status().isBadRequest()).andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMessage").value("Message body failed validation"))
                .andExpect(jsonPath("$.details").value(containsString(field)))
                .andExpect(jsonPath("$.details").value(containsString("must not be null")));
    }

    @Test
    public void whenANonRequiredParameterIsEmpty_ShouldReturn400BadRequest() throws Exception {

        String transaction = TransactionJsonFixtures.emptyPreviousReference();

        RequestBuilder request = MockMvcRequestBuilders.post("/transaction/payment/initiate", transaction)
                .header("SIGNATURE", "123456").contentType(MediaType.APPLICATION_JSON).content(transaction);

        mockMvc.perform(request).andExpect(status().isBadRequest()).andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMessage").value("Message body failed validation"))
                .andExpect(jsonPath("$.details").value(containsString("previousTransactionReference")))
                .andExpect(jsonPath("$.details").value(containsString("must not be empty")));
    }

    @Test
    public void whenAllFieldsPassValidation_ShouldPersitTheTransaction() throws Exception {

        String transactionJson = TransactionJsonFixtures.simple();

        RequestBuilder request = MockMvcRequestBuilders.post("/transaction/payment/initiate", transactionJson)
                .header("SIGNATURE", "123456").contentType(MediaType.APPLICATION_JSON).content(transactionJson);

        mockMvc.perform(request);

        Transaction persistedTransction = transactionRepo.findByTransactionReferenceAndSenderBankCode("000123321",
                "01");

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
    public void whenTryingToPerfomAnAlreadyExistingTransaction_ShouldReturn400BadRequest() throws Exception {
        String transactionJson = TransactionJsonFixtures.alreadyExistingTransaction();

        RequestBuilder request = MockMvcRequestBuilders.post("/transaction/payment/initiate", transactionJson)
                .header("SIGNATURE", "123456").contentType(MediaType.APPLICATION_JSON).content(transactionJson);

        mockMvc.perform(request).andExpect(status().isBadRequest()).andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMessage").value("Duplicated transaction"))
                .andExpect(jsonPath("$.details").value(containsString("{01}")))
                .andExpect(jsonPath("$.details").value(containsString("{9999999999}")));
    }

    @Test
    public void whenThePreviousTransactionReferenceDoesNotMatchAValidTransaction_ShouldReturn400BadRequest()
            throws Exception {

        String transactionJson = TransactionJsonFixtures.notExistingPrevioiusTransaction();

        RequestBuilder request = MockMvcRequestBuilders.post("/transaction/payment/initiate", transactionJson)
                .header("SIGNATURE", "123456").contentType(MediaType.APPLICATION_JSON).content(transactionJson);

        mockMvc.perform(request).andExpect(status().isBadRequest()).andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMessage").value("Transaction not found"))
                .andExpect(jsonPath("$.details").value(containsString("{35}")))
                .andExpect(jsonPath("$.details").value(containsString("{3213213211}")));
    }
}
