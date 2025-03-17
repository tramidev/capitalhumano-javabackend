package com.sensormanager.iot.adapter;

import com.sensormanager.iot.dto.CompanyDTO;
import com.sensormanager.iot.model.Company;

public class CompanyDataAdapter {

    public static CompanyDTO toDTO(Company Company) {
        return new CompanyDTO(
            Company.getId(),
            Company.getCompanyApiKey(),
            Company.getCompanyName(),
            Company.getCompanyStatus(),
            Company.getCompanyCreatedAt()
        );
    }

    public static Company toEntity(CompanyDTO CompanyDTO) {
    	
        return new Company(
        	CompanyDTO.getId(),
        	CompanyDTO.getCompanyApiKey(),
        	CompanyDTO.getCompanyName(),
        	CompanyDTO.getCompanyStatus(),
        	CompanyDTO.getCompanyCreatedAt()
        );
    }
}
