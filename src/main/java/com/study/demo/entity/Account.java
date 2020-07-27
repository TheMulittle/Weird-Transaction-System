package com.study.demo.entity;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotEmpty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Embeddable
public class Account {

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