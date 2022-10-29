package com.example.demo.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "DC_CASES")
public class DcCaseEntity {
	
	@Id
	@GeneratedValue
		
	private Long CaseNum;
	private Integer appId;		
	private Integer planId;		

}
