package com.sensormanager.iot.controller;

import com.sensormanager.iot.dto.CompanyDTO;
import com.sensormanager.iot.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/companies")
public class CompanyController {

	@Autowired
	private CompanyService companyService;

	@GetMapping
	public ResponseEntity<List<CompanyDTO>> findAll() {
		List<CompanyDTO> companies = companyService.findAll();
		if (companies.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(companies);
	}

	@GetMapping("/{id}")
	public ResponseEntity<CompanyDTO> findById(@PathVariable Long id) {
		CompanyDTO companyDTO = companyService.findById(id);
		if (companyDTO.getId() == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The company ID: " + id + " does not exist.");
		}
		return ResponseEntity.ok(companyDTO);
	}

	@PostMapping
	public ResponseEntity<CompanyDTO> create(@RequestBody CompanyDTO companyDTO) {
		CompanyDTO insertedCompany = companyService.create(companyDTO);
		if (insertedCompany.getId() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The company was not inserted.");
		}
		return ResponseEntity.ok(insertedCompany);
	}

	@PutMapping
	public ResponseEntity<CompanyDTO> update(@RequestBody CompanyDTO companyDTO) {
		CompanyDTO updatedCompany = companyService.update(companyDTO);
		if (updatedCompany.getId() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The company was not updated.");
		}
		return ResponseEntity.ok(updatedCompany);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<CompanyDTO> deleteById(@PathVariable Long id) {
		CompanyDTO deletedCompany = companyService.deleteById(id);
		if (deletedCompany.getId() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The company was not disabled.");
		}
		return ResponseEntity.ok(deletedCompany);
	}
}
