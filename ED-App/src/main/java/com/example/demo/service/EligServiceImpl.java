package com.example.demo.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.binding.CitizenApp;
import com.example.demo.entity.CitizenAppEntity;
import com.example.demo.entity.CoTriggerEntity;
import com.example.demo.entity.DcCaseEntity;
import com.example.demo.entity.DcChildrenEntity;
import com.example.demo.entity.DcEducationEntity;
import com.example.demo.entity.DcIncomeEntity;
import com.example.demo.entity.EligDtlsEntity;
import com.example.demo.entity.PlanEntity;
import com.example.demo.repository.CitizenAppRepo;
import com.example.demo.repository.CoTriggerRepo;
import com.example.demo.repository.DcCaseRepo;
import com.example.demo.repository.DcChildrenRepo;
import com.example.demo.repository.DcEducationRepo;
import com.example.demo.repository.DcIncomeRepo;
import com.example.demo.repository.EligDtlsRepo;
import com.example.demo.repository.PlanRepository;
import com.example.demo.response.EligResponse;

public class EligServiceImpl implements EligService {
	
	@Autowired
	private DcCaseRepo dcCaseRepo;
	
	@Autowired
	private PlanRepository planRepo;
	
	@Autowired
	private DcIncomeRepo incomeRepo;
	
	@Autowired
	private DcChildrenRepo childRepo;
	
	@Autowired
	private CitizenAppRepo appRepo;
	
	@Autowired
	 private DcEducationRepo eduRepo;
	
	@Autowired
	private EligDtlsRepo eligRepo;
	
	@Autowired
	private CoTriggerRepo coTrgRepo;
	
	

	@Override
	public EligResponse determineEligibility(Long caseNum) {
		
		Optional<DcCaseEntity> caseEntity = dcCaseRepo.findById(caseNum);
		Integer planId = null;
		String planName = null;
		Integer appId = null;
		
		if(caseEntity.isPresent()) {
			DcCaseEntity dcCaseEntity = caseEntity.get();
			
			 planId = dcCaseEntity.getPlanId();
			 appId = dcCaseEntity.getAppId();
		}
		
		Optional<PlanEntity> planEntity = planRepo.findById(planId);
		
		if(planEntity.isPresent()) {
			PlanEntity plan = planEntity.get();
			planName = plan.getPlanName();
			
		}
		
		Optional<CitizenAppEntity> app= appRepo.findById(appId);
		Integer age = 0;
		CitizenAppEntity citizenAppEntity = null;
		if(app.isPresent()) {
			 citizenAppEntity = app.get();
			LocalDate dob = citizenAppEntity.getChildDob();
			LocalDate now = LocalDate.now();
		
			 age  = Period.between(dob, now).getYears();
		}
		
	EligResponse eligResponse = executePlanConditions( caseNum,  planName, age);
		
		//logic to store the data in database
		
		EligDtlsEntity eligEntity = new EligDtlsEntity();
	
		BeanUtils.copyProperties(eligResponse, eligEntity);
		
		eligEntity.setCaseNum(caseNum);
		eligEntity.setHolderName(citizenAppEntity.getFullname());
		eligEntity.setHolderSsn(citizenAppEntity.getSsn());
		
		eligRepo.save(eligEntity);
		
		CoTriggerEntity coEntity = new CoTriggerEntity();
		coEntity.setCaseNum(caseNum);
		coEntity.setTrgStatus("pending");
		coTrgRepo.save(coEntity);
		
		return eligResponse;
		
	}
		
		
	
	private EligResponse executePlanConditions(Long caseNum, String planName, Integer appId) {
		
		EligResponse response = new EligResponse();
			response.setPlanName(planName);
			DcIncomeEntity income = incomeRepo.findByCaseNum(caseNum);
		
			if("SNAP".equals(planName)) {
				Double empIncome = income.getEmpIncome();
			if(empIncome <= 300) {
				response.setPlanStatus("AP");
			
			}else {
				response.setPlanStatus("DN");
				response.setDenialReason("High Income");
			}
			
		}else if("CCAP".equals(planName)) {
			boolean ageCondition = true;
			boolean kidsCountCondition = false;
			List<DcChildrenEntity> childs = childRepo.findByCaseNum(caseNum);
			if(!childs.isEmpty()) {
				kidsCountCondition = true;
				for(DcChildrenEntity entity : childs) {
					Integer childAge = entity.getChildAge();
					if(childAge > 16) {
						ageCondition = false;
						break;
					}
				}
			}
			
			if(income.getEmpIncome() <= 300 && kidsCountCondition && ageCondition) {
				
				response.setPlanStatus("AP");
			}else {
				response.setPlanStatus("DN");
				response.setDenialReason("Not Satisfied Business Rules");
			}
			
		}else if("Medicaid".equals(planName)) {
			
			Double empIncome = income.getEmpIncome();
			Double propertyIncome = income.getPropertyIncome();
			
			if(empIncome <= 300 && propertyIncome == 0) { 
				response.setPlanStatus("AP");
			}else {
				response.setPlanStatus("DN");
				response.setDenialReason("High Income");
			}
			
		}else if("Medicare".equals(planName)) {
			
			Optional<CitizenAppEntity> app= appRepo.findById(appId);
			if(app.isPresent()) {
				CitizenAppEntity citizenAppEntity = app.get();
				LocalDate dob = citizenAppEntity.getChildDob();
				LocalDate now = LocalDate.now();
				
				int age  = Period.between(dob, now).getYears();
				
				if (age >= 65) {
					response.setPlanStatus("AP");
					
				}else {
					response.setPlanStatus("DN");
				response.setDenialReason("Age Not Matched");	
				
				}
			}
			
			
		}else if("NJW".equals(planName)) {
			DcEducationEntity educationEntity = eduRepo.findByCaseNum(caseNum);
			Integer graduationYear = educationEntity.getGraduationYear();
			
			int currYear  = LocalDate.now().getYear();
			
			if (income.getEmpIncome() <= 0 && graduationYear < currYear) {
				
				response.setPlanStatus("AP");
			}else {
				response.setPlanStatus("DN");
				response.setDenialReason("Rules Not Satisfied");
			}
			
			}
			
			if(response.getPlanStatus().equals("AP")) {
			response.setPlanStartDate(LocalDate.now());
			response.setPlanEndDate(LocalDate.now().plusMonths(6));
			response.setBenefitAmt(350.00);
			}
			return response;
	}
}
