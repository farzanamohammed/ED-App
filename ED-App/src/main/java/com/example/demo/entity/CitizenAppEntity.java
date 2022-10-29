package com.example.demo.entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;

@Entity
@Data
@Table(name = "CITIZEN_APPS")
public class CitizenAppEntity {
	
	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY)
	private Integer appId;
	
	private String fullname;
	
	private String email;
	
	private Long phno;
	
	private String gender;
	
	private Integer ssn;
	
	private String stateName;
	@CreationTimestamp
	private LocalDate createdDate;
	@CreationTimestamp
	private LocalDate updatedDate;
	
	private String createdBy;
	
	private String updatedBy;

	public LocalDate childDob;
		
	
	
	
	
	
	

}
