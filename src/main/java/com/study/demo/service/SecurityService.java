package com.study.demo.service;

import com.study.demo.entity.BankLink;
import com.study.demo.exception.IpAdressNotKnownException;
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

    public String retrieveBankCodeByBankIP(String bankIP) throws IpAdressNotKnownException {

        BankLink foundBankLink = bankLinkRepository.findByBankIP(bankIP);

        if (foundBankLink == null) {
            throw new IpAdressNotKnownException();
        } else {
            return foundBankLink.getBankCode();
        }
    }
}