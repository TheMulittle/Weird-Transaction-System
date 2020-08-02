package com.study.demo.service;

import com.study.demo.exception.SenderNotValidException;
import com.study.demo.repository.BankLinkRepository;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SecurityService {

    private final BankLinkRepository bankLinkRepository;

    public void validateSenderBankAuthenticity(String senderIP, String bankCode) throws SenderNotValidException {
        if (bankLinkRepository.findByBankCodeAndBankIP(bankCode, senderIP) == null) {
            throw new SenderNotValidException(senderIP, bankCode);
        }
    }

}