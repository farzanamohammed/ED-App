package com.example.demo.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.DcChildrenEntity;

public interface DcChildrenRepo extends JpaRepository<DcChildrenEntity, Serializable> {

	
	public List<DcChildrenEntity> findByCaseNum(Long caseNum);
}
