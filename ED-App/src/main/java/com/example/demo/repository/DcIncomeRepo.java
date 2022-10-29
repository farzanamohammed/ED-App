package com.example.demo.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.DcIncomeEntity;

public interface DcIncomeRepo extends JpaRepository<DcIncomeEntity, Serializable> {

	
	public DcIncomeEntity findByCaseNum(Long caseNum);

	
	
	
	
	

	

}
