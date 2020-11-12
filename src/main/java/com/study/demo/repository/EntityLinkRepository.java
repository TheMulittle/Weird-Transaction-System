package com.study.demo.repository;

import org.springframework.transaction.annotation.Transactional;

import com.study.demo.entity.EntityLink;

import org.springframework.data.repository.CrudRepository;

@Transactional
public interface EntityLinkRepository extends CrudRepository<EntityLink, Long> {
    public EntityLink findByEntityCodeAndEntityIP(String entityCode, String entityIP);

    public EntityLink findByEntityIP(String entityIP);
}