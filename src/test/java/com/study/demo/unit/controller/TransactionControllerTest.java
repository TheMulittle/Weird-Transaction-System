package com.study.demo.unit.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.study.demo.controller.TransactionController;
import com.study.demo.dto.TransactionDTO;
import com.study.demo.dto.TransactionQueryResponses;
import com.study.demo.exception.AmountGreaterThanMaximumException;
import com.study.demo.exception.AmountSmallerThanMinimumException;
import com.study.demo.exception.IpAdressNotKnownException;
import com.study.demo.exception.SameBankException;
import com.study.demo.exception.SenderNotValidException;
import com.study.demo.service.SecurityService;
import com.study.demo.service.TransactionService;
import com.study.demo.fixtures.transaction.TransactionJsonFixtures;
import com.study.demo.fixtures.transaction.TransactionQueryResponseFixtures;
import com.study.demo.model.DirectionEnum;
import com.study.demo.model.StateEnum;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private SecurityService securityService;

    @MockBean
    private TransactionService transactionService;

    @Mock
    private TransactionDTO mockTransaction;

    private final static String PAYMENT_ENDPOINT = "/transaction/payment";

    @Test
    public void shouldReturn401_whenTheSenderBankIPdiffersFromTheBankCode() throws Exception {

        RequestBuilder request = MockMvcRequestBuilders.post(PAYMENT_ENDPOINT).contentType(MediaType.APPLICATION_JSON)
                .header("SIGNATURE", "dummy").content(TransactionJsonFixtures.simple());

        doThrow(new SenderNotValidException("127.0.0.1", "01")).when(securityService)
                .validateSenderBankAuthenticity("127.0.0.1", "01");

        mvc.perform(request).andExpect(status().isUnauthorized()).andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.status").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.details").value(containsString("{127.0.0.1}")))
                .andExpect(jsonPath("$.details").value(containsString("{01}")));
    }

    @Test
    public void shouldReturn400_whenSenderAndReceiverPertainToSameBank() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.post(PAYMENT_ENDPOINT).contentType(MediaType.APPLICATION_JSON)
                .header("SIGNATURE", "dummy").content(TransactionJsonFixtures.sameBankTransaction());

        doThrow(new SameBankException("01")).when(transactionService).transact(any());

        mvc.perform(request).andExpect(status().isBadRequest()).andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.details").value(containsString("{01}")));
    }

    @Test
    public void shouldReturn400_whenAmountIsHigherThanTheMaximumAllowed() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.post(PAYMENT_ENDPOINT).contentType(MediaType.APPLICATION_JSON)
                .header("SIGNATURE", "dummy").content(TransactionJsonFixtures.simple());

        doThrow(new AmountGreaterThanMaximumException(50L, 500L)).when(transactionService).transact(any());

        mvc.perform(request).andExpect(status().isBadRequest()).andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.details").value(containsString("{50}")))
                .andExpect(jsonPath("$.details").value(containsString("{500}")));
    }

    @Test
    public void shouldReturn400_whenAmountIsLowerThanTheMinimumAllowed() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.post(PAYMENT_ENDPOINT).contentType(MediaType.APPLICATION_JSON)
                .header("SIGNATURE", "dummy").content(TransactionJsonFixtures.simple());

        doThrow(new AmountSmallerThanMinimumException(0L, 0L)).when(transactionService).transact(any());

        mvc.perform(request).andExpect(status().isBadRequest()).andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.details").value(containsString("{0}")))
                .andExpect(jsonPath("$.details").value(containsString("{0}")));
    }

    @Test
    public void shouldReturnListOfQueryResponses_whenQueriedWithoutStateAndDirection() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get(PAYMENT_ENDPOINT).contentType(MediaType.APPLICATION_JSON)
                .header("SIGNATURE", "dummy");

        TransactionQueryResponses queryResponses = TransactionQueryResponseFixtures.simpleQueryResponses();

        when(securityService.retrieveBankCodeByBankIP(any())).thenReturn("35");

        when(transactionService.retrieveTransactionsByStateAndDiretionAndBankCode(null, null, "35"))
                .thenReturn(queryResponses);

        mvc.perform(request).andExpect(status().isOk()).andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.transactions").isArray())
                .andExpect(jsonPath("$.transactions[0].status").value(containsString("INITIATED")));
    }

    @Test
    public void shouldReturnListOfQueryResponses_whenQueriedByStateAndDirection() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get(PAYMENT_ENDPOINT).contentType(MediaType.APPLICATION_JSON)
                .param("direction", "INWARD").param("state", "INITIATED").header("SIGNATURE", "dummy");

        TransactionQueryResponses queryResponses = TransactionQueryResponseFixtures.simpleQueryResponses();

        when(securityService.retrieveBankCodeByBankIP(any())).thenReturn("35");

        when(transactionService.retrieveTransactionsByStateAndDiretionAndBankCode(StateEnum.INITIATED,
                DirectionEnum.INWARD, "35")).thenReturn(queryResponses);

        mvc.perform(request).andExpect(status().isOk()).andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.transactions").isArray())
                .andExpect(jsonPath("$.transactions[0].status").value(containsString("INITIATED")));
    }

    @Test
    public void shouldReturnListOfQueryResponses_whenQueriedByEmptyStateAndEmptyDirection() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get(PAYMENT_ENDPOINT).contentType(MediaType.APPLICATION_JSON)
                .param("direction", "").param("state", "").header("SIGNATURE", "dummy");

        TransactionQueryResponses queryResponses = TransactionQueryResponseFixtures.simpleQueryResponses();

        when(securityService.retrieveBankCodeByBankIP(any())).thenReturn("35");

        when(transactionService.retrieveTransactionsByStateAndDiretionAndBankCode(null, null, "35"))
                .thenReturn(queryResponses);

        mvc.perform(request).andExpect(status().isOk()).andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.transactions").isArray())
                .andExpect(jsonPath("$.transactions[0].status").value(containsString("INITIATED")));
    }

    @Test
    public void shouldReturn401_whenTheCallerIpIsNotKnown() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get(
                PAYMENT_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON).header("SIGNATURE", "dummy");

        doThrow(new IpAdressNotKnownException()).when(securityService).retrieveBankCodeByBankIP(any());

        mvc.perform(request).andExpect(status().isUnauthorized()).andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.status").value("UNAUTHORIZED"));

    }
}