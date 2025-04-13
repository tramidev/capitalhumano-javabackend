package com.sensormanager.iot.controller;

import java.util.Arrays;
import java.util.Collections;

import com.sensormanager.iot.security.AuthenticatedService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sensormanager.iot.dto.CompanyDTO;
import com.sensormanager.iot.service.CompanyService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CompanyControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CompanyService companyService;

    @Mock
    private AuthenticatedService authenticatedService;

    @InjectMocks
    private CompanyController companyController;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(companyController).build();
        objectMapper = new ObjectMapper();

        // Simula que el usuario autenticado es ROOT en todos los tests
        when(authenticatedService.isRootUser()).thenReturn(true);
    }

    @Test
    void testFindAll() throws Exception {
        CompanyDTO company1 = new CompanyDTO();
        company1.setId(1L);
        company1.setCompanyName("Company A");

        CompanyDTO company2 = new CompanyDTO();
        company2.setId(2L);
        company2.setCompanyName("Company B");

        when(companyService.findAll()).thenReturn(Arrays.asList(company1, company2));

        mockMvc.perform(get("/companies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].companyName").value("Company A"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].companyName").value("Company B"));

        when(companyService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/companies"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testFindById() throws Exception {
        CompanyDTO company = new CompanyDTO();
        company.setId(1L);
        company.setCompanyName("Company A");

        when(companyService.findById(1L)).thenReturn(company);

        mockMvc.perform(get("/companies/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.companyName").value("Company A"));

        when(companyService.findById(2L)).thenReturn(new CompanyDTO());

        mockMvc.perform(get("/companies/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreate() throws Exception {
        CompanyDTO company = new CompanyDTO();
        company.setCompanyName("Company A");

        CompanyDTO createdCompany = new CompanyDTO();
        createdCompany.setId(1L);
        createdCompany.setCompanyName("Company A");

        when(companyService.create(any(CompanyDTO.class))).thenReturn(createdCompany);

        mockMvc.perform(post("/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(company)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.companyName").value("Company A"));
    }

    @Test
    void testUpdate() throws Exception {
        CompanyDTO company = new CompanyDTO();
        company.setId(1L);
        company.setCompanyName("Updated Company");

        when(companyService.update(any(CompanyDTO.class))).thenReturn(company);

        mockMvc.perform(put("/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(company)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.companyName").value("Updated Company"));
    }

    @Test
    void testDeleteById() throws Exception {
        CompanyDTO company = new CompanyDTO();
        company.setId(1L);

        when(companyService.deleteById(1L)).thenReturn(company);

        mockMvc.perform(delete("/companies/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        when(companyService.deleteById(2L)).thenReturn(new CompanyDTO());

        mockMvc.perform(delete("/companies/2"))
                .andExpect(status().isBadRequest());
    }
}
