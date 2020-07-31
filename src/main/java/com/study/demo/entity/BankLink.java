package com.study.demo.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Entity
public class BankLink {
    @Id
    @NotEmpty
    private String bankCode;

    @NotEmpty
    private String bankIP;
}