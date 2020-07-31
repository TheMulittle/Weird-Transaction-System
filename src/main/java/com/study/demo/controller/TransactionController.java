package com.study.demo.controller;

import javax.servlet.http.HttpServletRequest;

import com.study.demo.dto.TransactionDTO;
import com.study.demo.service.SecurityService;
import com.study.demo.service.TransactionService;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/transaction")
public class TransactionController {

    private final SecurityService securityService;
    private final TransactionService transactionService;

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public void performTransaction(@RequestBody TransactionDTO transaction,
            @RequestHeader("SIGNATURE") String signature, HttpServletRequest request) {

        securityService.validateSenderBankAuthenticity(transaction.getSenderAccount().getBankCode(),
                request.getRemoteAddr());
        transactionService.transact(transaction);
    }

}