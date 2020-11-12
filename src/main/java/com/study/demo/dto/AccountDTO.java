package com.study.demo.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {

    @NotBlank
    String ownerFirstName;

    @NotBlank
    String ownerLastName;

    @NotBlank
    String ownerDocumentNumber;

    @NotBlank
    String accountNumber;

    @NotBlank
    String entityCode;
}