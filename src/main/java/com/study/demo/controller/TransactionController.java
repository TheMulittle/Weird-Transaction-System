package com.study.demo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.study.demo.dto.TransactionDTO;
import com.study.demo.dto.TransactionQueryResponses;
import com.study.demo.model.DirectionEnum;
import com.study.demo.model.StateEnum;
import com.study.demo.service.SecurityService;
import com.study.demo.service.TransactionService;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transaction")
@Validated
public class TransactionController {

    private final SecurityService securityService;
    private final TransactionService transactionService;

    @PostMapping("/payment")
    @ResponseStatus(HttpStatus.CREATED)
    @Produces(MediaType.APPLICATION_JSON)
    public void performTransaction(@Valid @RequestBody TransactionDTO transaction,
            @RequestHeader("SIGNATURE") String signature, HttpServletRequest request) {

        securityService.validateSenderBankAuthenticity(request.getRemoteAddr(),
                transaction.getSenderAccount().getBankCode());

        transactionService.transact(transaction);
    }

    @GetMapping("/payment")
    @ResponseStatus(HttpStatus.OK)
    @Produces(MediaType.APPLICATION_JSON)
    public TransactionQueryResponses getTransaction(@RequestParam(required=false) StateEnum state,
            @RequestParam(required = false) DirectionEnum direction, @RequestHeader("SIGNATURE") String signature,
            HttpServletRequest request) {

        String bankCode = securityService.retrieveBankCodeByBankIP(request.getRemoteAddr());

        return transactionService.retrieveTransactionsByStateAndDiretionAndBankCode(state, direction, bankCode);
    }
}