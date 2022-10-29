package com.example.demo.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.DcEducationEntity;

public interface DcEducationRepo extends JpaRepository<DcEducationEntity, Serializable>{

	public DcEducationEntity findByCaseNum(Long caseNum);
	
}
