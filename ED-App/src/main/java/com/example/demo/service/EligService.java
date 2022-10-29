package com.example.demo.service;


import org.springframework.stereotype.Service;

import com.example.demo.repository.DcCaseRepo;
import com.example.demo.response.EligResponse;

@Service
public interface EligService {
	
	
	
	public EligResponse determineEligibility(Long caseNum);

}
