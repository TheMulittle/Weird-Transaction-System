package com.study.demo.unit.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.study.demo.controller.TransactionController;
import com.study.demo.exception.SenderNotValidException;
import com.study.demo.service.SecurityService;
import com.study.demo.service.TransactionService;
import com.study.demo.unit.fixtures.transaction.TransactionJsonFixtures;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@ExtendWith(SpringExtension.class)
@AutoConfigureJsonTesters
@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private SecurityService securityService;

    @MockBean
    private TransactionService transactionService;

    @Test
    public void shouldReturn401_whenTheSenderBankIPdiffersFromTheBankCode() throws Exception {

        RequestBuilder request = MockMvcRequestBuilders.post("/transaction/payment/initiate")
                .contentType(MediaType.APPLICATION_JSON).header("SIGNATURE", "12345678912332156498732156487")
                .content(TransactionJsonFixtures.simple());

        doThrow(new SenderNotValidException("127.0.0.1", "01")).when(securityService)
                .validateSenderBankAuthenticity("127.0.0.1", "01");

        mvc.perform(request).andExpect(status().isUnauthorized()).andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.status").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.details").value(containsString("{127.0.0.1}")))
                .andExpect(jsonPath("$.details").value(containsString("{01}")));
    }

    @Test
    public void shouldReturn400_thereIsAMissingParameter() {

    }

    @Test
    public void shouldReturn400_whenSenderAndReceiverPertainToSameBank() {

    }

    @Test
    public void shouldReturn400_whenAmountIsHigherThanTheMaximumAllowed() {

    }

    @Test
    public void shouldReturn400_whenAmountIsZero() {

    }
}