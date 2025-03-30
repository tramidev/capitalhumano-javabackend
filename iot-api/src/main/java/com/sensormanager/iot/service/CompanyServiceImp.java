package com.sensormanager.iot.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sensormanager.iot.adapter.CompanyDataAdapter;
import com.sensormanager.iot.dto.CompanyDTO;
import com.sensormanager.iot.model.Company;
import com.sensormanager.iot.repository.CompanyRepository;
import com.sensormanager.iot.utils.GenerateApiKey;

@Service
public class CompanyServiceImp implements CompanyService {

	@Autowired
    private CompanyRepository companyRepository;
	
	@Override
	public List<CompanyDTO> findAll() {
        List<Company> companies = companyRepository.findByCompanyStatusTrue();
        if (companies == null) {
            return new ArrayList<CompanyDTO>();
        }
        return companies.stream().map(CompanyDataAdapter::toDTO).collect(Collectors.toList());
    }

	@Override
	public CompanyDTO findById(Long id) {
		Company company = companyRepository.findByIdAndCompanyStatusTrue(id).orElse(null);
        if (company == null) {
            return new CompanyDTO();
        }
        return CompanyDataAdapter.toDTO(company);
	}

	@Override
	public CompanyDTO create(CompanyDTO companyDto) {
		if (companyRepository.existsByCompanyName(companyDto.getCompanyName())) {
            return new CompanyDTO();
        }		
		companyDto.setCompanyApiKey(GenerateApiKey.generate(companyDto.getCompanyName()));
		companyDto.setCompanyStatus(true);		
        Company company = CompanyDataAdapter.toEntity(companyDto);       
        Company companyAdded = companyRepository.save(company);
        
        if (companyAdded == null) {
            return new CompanyDTO();
        }
        return CompanyDataAdapter.toDTO(companyAdded);
	}

	@Override
	public CompanyDTO update(CompanyDTO companyDto) {
		
		Company companyWillUpdated = companyRepository.findById(companyDto.getId()).orElse(null);	
		if (companyWillUpdated == null) {
            return new CompanyDTO();
        }
		companyWillUpdated.setCompanyName(companyDto.getCompanyName() != null && companyDto.getCompanyName().length() > 0 ? companyDto.getCompanyName() : companyWillUpdated.getCompanyName());
		companyWillUpdated.setCompanyStatus(companyDto.getCompanyStatus() != null ? companyDto.getCompanyStatus() : companyWillUpdated.getCompanyStatus());
        Company companyUpdated = companyRepository.save(companyWillUpdated);
        if (companyUpdated == null) {
            return new CompanyDTO();
        }
        return CompanyDataAdapter.toDTO(companyUpdated);
	}
	
	@Autowired
	LocationServiceImp locationService;
	
	@Override
	public CompanyDTO deleteById(Long id) {
		Company companyWillDeleted = companyRepository.findById(id).orElse(null);
        if (companyWillDeleted == null) {
            return new CompanyDTO();
        }
        companyWillDeleted.setCompanyStatus(false);
       
        if (companyWillDeleted.getLocations() != null) {
        	
        	companyWillDeleted.getLocations().forEach(location -> locationService.deleteById(location.getId()));
        }
        
        Company companyDeleted= companyRepository.save(companyWillDeleted);
        if (companyDeleted == null) {
            return new CompanyDTO();
        }
        return CompanyDataAdapter.toDTO(companyDeleted);
	}

}
