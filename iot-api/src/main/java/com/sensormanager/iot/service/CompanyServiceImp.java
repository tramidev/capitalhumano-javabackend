package com.sensormanager.iot.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.sensormanager.iot.security.AuthenticatedService;
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

    @Autowired
    private AuthenticatedService authenticatedService;

    @Autowired
    private LocationServiceImp locationService;

    @Override
    public List<CompanyDTO> findAll() {
        if (authenticatedService.isRootUser()) {
            List<Company> companies = companyRepository.findByCompanyStatusTrue();
            if (companies == null) return new ArrayList<>();
            return companies.stream().map(CompanyDataAdapter::toDTO).collect(Collectors.toList());
        }

        Company ownCompany = authenticatedService.getAuthenticatedCompany();
        if (ownCompany == null) return new ArrayList<>();

        CompanyDTO dto = CompanyDataAdapter.toDTO(ownCompany);
        List<CompanyDTO> result = new ArrayList<>();
        result.add(dto);
        return result;
    }

    @Override
    public CompanyDTO findById(Long id) {
        Company company = companyRepository.findByIdAndCompanyStatusTrue(id).orElse(null);
        if (company == null) return new CompanyDTO();

        if (!authenticatedService.isRootUser()) {
            Company ownCompany = authenticatedService.getAuthenticatedCompany();
            if (!company.getId().equals(ownCompany.getId())) return new CompanyDTO();
        }

        return CompanyDataAdapter.toDTO(company);
    }

    @Override
    public CompanyDTO create(CompanyDTO companyDto) {
        if (!authenticatedService.isRootUser()) return new CompanyDTO();

        if (companyRepository.existsByCompanyName(companyDto.getCompanyName())) {
            return new CompanyDTO();
        }

        companyDto.setCompanyApiKey(GenerateApiKey.generate(companyDto.getCompanyName()));
        companyDto.setCompanyStatus(true);
        Company company = CompanyDataAdapter.toEntity(companyDto);
        Company companyAdded = companyRepository.save(company);

        return companyAdded != null ? CompanyDataAdapter.toDTO(companyAdded) : new CompanyDTO();
    }

    @Override
    public CompanyDTO update(CompanyDTO companyDto) {
        Company companyToUpdate = companyRepository.findById(companyDto.getId()).orElse(null);
        if (companyToUpdate == null) {
            return new CompanyDTO();
        }

        if (!authenticatedService.isRootUser()) {
            Company userCompany = authenticatedService.getAuthenticatedCompany();
            if (userCompany == null || !companyToUpdate.getId().equals(userCompany.getId())) {
                return new CompanyDTO();
            }
        }

        if (companyDto.getCompanyName() != null && !companyDto.getCompanyName().isEmpty()) {
            companyToUpdate.setCompanyName(companyDto.getCompanyName());
        }

        if (companyDto.getCompanyStatus() != null) {
            companyToUpdate.setCompanyStatus(companyDto.getCompanyStatus());
        }

        Company companyUpdated = companyRepository.save(companyToUpdate);
        return companyUpdated != null ? CompanyDataAdapter.toDTO(companyUpdated) : new CompanyDTO();
    }

    @Override
    public CompanyDTO deleteById(Long id) {
        if (!authenticatedService.isRootUser()) return new CompanyDTO();

        Company company = companyRepository.findById(id).orElse(null);
        if (company == null) return new CompanyDTO();

        company.setCompanyStatus(false);

        if (company.getLocations() != null) {
            company.getLocations().forEach(location -> locationService.deleteById(location.getId()));
        }

        Company companyDeleted = companyRepository.save(company);
        return companyDeleted != null ? CompanyDataAdapter.toDTO(companyDeleted) : new CompanyDTO();
    }
}
