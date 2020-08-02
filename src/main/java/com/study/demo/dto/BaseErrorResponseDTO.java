package com.study.demo.dto;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BaseErrorResponseDTO {

    HttpStatus status;

    String errorMessage;

    String details;
}