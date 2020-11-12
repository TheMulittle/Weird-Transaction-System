package com.study.demo.service;

import com.study.demo.entity.EntityLink;
import com.study.demo.exception.IpAdressNotKnownException;
import com.study.demo.exception.SenderNotValidException;
import com.study.demo.repository.EntityLinkRepository;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SecurityService {

    private final EntityLinkRepository entityLinkRepository;

    public void validateSenderEntityAuthenticity(String senderIP, String entityCode) throws SenderNotValidException {
        if (entityLinkRepository.findByEntityCodeAndEntityIP(entityCode, senderIP) == null) {
            throw new SenderNotValidException(senderIP, entityCode);
        }
    }

    public String retrieveEntityCodeByEntityIP(String entityIP) throws IpAdressNotKnownException {

        EntityLink foundEntityLink = entityLinkRepository.findByEntityIP(entityIP);

        if (foundEntityLink == null) {
            throw new IpAdressNotKnownException();
        } else {
            return foundEntityLink.getEntityCode();
        }
    }
}