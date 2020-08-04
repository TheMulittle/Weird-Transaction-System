package com.study.demo.unit.service;

import static org.mockito.Mockito.when;

import com.study.demo.entity.BankLink;
import com.study.demo.exception.SenderNotValidException;
import com.study.demo.repository.BankLinkRepository;
import com.study.demo.service.SecurityService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SecurityServiceTest {

    @Mock
    BankLinkRepository bankLinkRepository;

    @InjectMocks
    SecurityService securityService;

    private final String BANK_CODE = "01";
    private final String BANK_IP = "172.17.2.1";

    @Test
    public void shouldReturnException_whenBankIPDoesNotMatchTheIPForBankCode() {

        when(bankLinkRepository.findByBankCodeAndBankIP(BANK_CODE, BANK_IP)).thenReturn(null);

        Assertions.assertThrows(SenderNotValidException.class, () -> {
            securityService.validateSenderBankAuthenticity(BANK_IP, BANK_CODE);
        });

    }

    @Test
    public void shouldNotReturnException_whenBankIPMatchesTheIPForBankCode() {

        BankLink bankLink = BankLink.builder().bankCode(BANK_CODE).bankIP(BANK_IP).build();

        when(bankLinkRepository.findByBankCodeAndBankIP(BANK_CODE, BANK_IP)).thenReturn(bankLink);
        securityService.validateSenderBankAuthenticity(BANK_IP, BANK_CODE);
    }

}