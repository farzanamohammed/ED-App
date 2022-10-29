package com.example.demo.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table (name = "DC_INCOME")

public class DcIncomeEntity {
	
	@Id
	@GeneratedValue
	private Integer incomeId;	
	private Long CaseNum;		
	private Double empIncome;		
	private Double propertyIncome;

}
