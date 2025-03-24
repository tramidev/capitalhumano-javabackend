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

    public static Company toEntity(CompanyDTO companyDTO) {
    	return Company.builder()
                .id(companyDTO.getId())
                .companyApiKey(companyDTO.getCompanyApiKey())
                .companyName(companyDTO.getCompanyName())
                .companyStatus(companyDTO.getCompanyStatus())
                .companyCreatedAt(companyDTO.getCompanyCreatedAt())
                .build();
    }
}
