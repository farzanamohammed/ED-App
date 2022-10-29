package com.example.demo.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.CoTriggerEntity;

public interface CoTriggerRepo extends JpaRepository<CoTriggerEntity, Serializable> {

}
