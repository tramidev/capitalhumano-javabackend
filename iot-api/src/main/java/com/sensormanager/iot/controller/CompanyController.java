package com.sensormanager.iot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.sensormanager.iot.service.CompanyService;
import com.sensormanager.iot.dto.CompanyDTO;

@RestController
@RequestMapping("/companies")
public class CompanyController {

	@Autowired
    private CompanyService CompanyService;
	
	 @GetMapping
	    public ResponseEntity<List<CompanyDTO>> findAll(){
	        List<CompanyDTO> CompanysDto = CompanyService.findAll();
	        if(CompanysDto.size() == 0){
	            return ResponseEntity.status(HttpStatus.NO_CONTENT.value()).body(CompanysDto);
	        }
	        return ResponseEntity.status(HttpStatus.OK.value()).body(CompanysDto);
	    }

	    @GetMapping("/{id}")
	    public ResponseEntity<CompanyDTO> findById(@PathVariable Long id){
	        CompanyDTO CompanyDTO = CompanyService.findById(id);
	        if(CompanyDTO.getId() == null){
	        	throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The company ID: " + id + " does not exist.");
	        }
	        return ResponseEntity.status(HttpStatus.OK.value()).body(CompanyDTO);
	    }

	    @PostMapping
	    public ResponseEntity<CompanyDTO> create(@RequestBody CompanyDTO CompanyDTO){
	        CompanyDTO CompanyInsertado = CompanyService.create(CompanyDTO);
	        if(CompanyInsertado.getId() == null){
	        	throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The company was not inserted.");
	        }
	        return ResponseEntity.status(HttpStatus.OK.value()).body(CompanyInsertado);
	    }

	    @PutMapping
	    public ResponseEntity<CompanyDTO> update(@RequestBody CompanyDTO CompanyDTO){
	        CompanyDTO CompanyActualizado = CompanyService.update(CompanyDTO);
	        if(CompanyActualizado.getId() == null){
	        	throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The company was not updated.");
	        }
	        return ResponseEntity.status(HttpStatus.OK.value()).body(CompanyActualizado);
	    }

	    @DeleteMapping("/{id}")
	    public ResponseEntity<CompanyDTO> deleteById(@PathVariable Long id){
	        CompanyDTO CompanyEliminado = CompanyService.deleteById(id);
	        if(CompanyEliminado.getId() == null){
	        	throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The company was not disabled.");
	        }
	        return ResponseEntity.status(HttpStatus.OK.value()).body(CompanyEliminado);
	    }
}
