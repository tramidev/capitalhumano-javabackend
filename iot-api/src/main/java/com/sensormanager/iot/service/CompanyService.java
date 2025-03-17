package com.sensormanager.iot.service;

import java.util.List;

import com.sensormanager.iot.dto.CompanyDTO;


public interface CompanyService {

	public List<CompanyDTO> findAll();

    public CompanyDTO findById(Long id);

    public CompanyDTO create(CompanyDTO cursoDto);

    public CompanyDTO update(CompanyDTO cursoDto);

    public CompanyDTO deleteById(Long id);
}
