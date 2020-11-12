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
@Table(name = "ENTITY_LINK")
@NoArgsConstructor
@AllArgsConstructor
public class EntityLink {

    @Id
    @NotEmpty
    @Column(name = "ENTITY_IP")
    private String entityIP;

    @NotEmpty
    @Column(name = "ENTITY_CODE")
    private String entityCode;
}