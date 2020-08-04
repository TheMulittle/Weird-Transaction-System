package com.study.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@Entity
@Table(name = "BANK_LINK")
@NoArgsConstructor
@AllArgsConstructor
public class BankLink {

    @Id
    @NotEmpty
    @Column(name = "BANK_IP")
    private String bankIP;

    @NotEmpty
    @Column(name = "BANK_CODE")
    private String bankCode;
}