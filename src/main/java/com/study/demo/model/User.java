package com.study.demo.model;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
@Embeddable
public class User {

    @NotEmpty
    String firstName;

    @NotEmpty
    String lastName;

    @NotEmpty
    String documentNumber;

    @NotEmpty
    String account;
}