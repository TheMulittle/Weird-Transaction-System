package com.study.demo.repository;

import org.springframework.transaction.annotation.Transactional;

import com.study.demo.entity.Configuration;

import org.springframework.data.repository.CrudRepository;

@Transactional
public interface ConfigurationRepository extends CrudRepository<Configuration, String> {
    public String findByName(String name);
}