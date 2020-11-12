package com.study.demo.unit.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.study.demo.entity.EntityLink;
import com.study.demo.exception.IpAdressNotKnownException;
import com.study.demo.exception.SenderNotValidException;
import com.study.demo.repository.EntityLinkRepository;
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
    EntityLinkRepository entityLinkRepository;

    @InjectMocks
    SecurityService securityService;

    private final String ENTITY_CODE = "01";
    private final String ENTITY_IP = "172.17.2.1";

    @Test
    public void shouldReturnException_whenEntityIPDoesNotMatchTheIPForEntityCode() {

        when(entityLinkRepository.findByEntityCodeAndEntityIP(ENTITY_CODE, ENTITY_IP)).thenReturn(null);

        Assertions.assertThrows(SenderNotValidException.class, () -> {
            securityService.validateSenderEntityAuthenticity(ENTITY_IP, ENTITY_CODE);
        });
    }

    @Test
    public void shouldNotReturnException_whenEntityIPMatchesTheIPForEntityCode() {

        EntityLink entityLink = EntityLink.builder().entityCode(ENTITY_CODE).entityIP(ENTITY_IP).build();

        when(entityLinkRepository.findByEntityCodeAndEntityIP(ENTITY_CODE, ENTITY_IP)).thenReturn(entityLink);
        securityService.validateSenderEntityAuthenticity(ENTITY_IP, ENTITY_CODE);
    }

    @Test
    public void shouldNotReturnException_whenAKnownIPIsGiven() {
        EntityLink entityLink = EntityLink.builder().entityCode(ENTITY_CODE).entityIP(ENTITY_IP).build();
        when(entityLinkRepository.findByEntityIP(ENTITY_IP)).thenReturn(entityLink);

        securityService.retrieveEntityCodeByEntityIP(ENTITY_IP);

        verify(entityLinkRepository).findByEntityIP(ENTITY_IP);
    }

    @Test
    public void shouldReturnException_whenEntityIPDoesNotExist() {

        when(entityLinkRepository.findByEntityIP(ENTITY_IP)).thenReturn(null);

        Assertions.assertThrows(IpAdressNotKnownException.class, () -> {
            securityService.retrieveEntityCodeByEntityIP(ENTITY_IP);
        });
    }

}