package com.study.demo.dto;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {

    @NotEmpty
    String ownerFirstName;

    @NotEmpty
    String ownerLastName;

    @NotEmpty
    String ownerDocumentNumber;

    @NotEmpty
    String accountNumber;

    @NotEmpty
    String bankCode;
}