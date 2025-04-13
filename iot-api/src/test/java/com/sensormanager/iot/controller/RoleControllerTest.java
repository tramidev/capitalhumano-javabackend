package com.sensormanager.iot.controller;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sensormanager.iot.dto.RoleDTO;
import com.sensormanager.iot.service.RoleService;
import org.springframework.web.server.ResponseStatusException;

class RoleControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RoleService roleService;

    @InjectMocks
    private RoleController roleController;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(roleController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testFindAll() throws Exception {
        RoleDTO role1 = new RoleDTO(1L, "Admin", "Description", 1742854217L);
        RoleDTO role2 = new RoleDTO(2L, "User", "Description", 1742854217L);

        when(roleService.findAll()).thenReturn(Arrays.asList(role1, role2));

        mockMvc.perform(get("/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].roleName").value("Admin"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].roleName").value("User"));

        when(roleService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/roles"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testFindById() throws Exception {
        RoleDTO role = new RoleDTO(1L, "Admin", "Description", 1742854217L);
        when(roleService.findById(1L)).thenReturn(role);
        mockMvc.perform(get("/roles/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.roleName").value("Admin"));
        when(roleService.findById(2L)).thenReturn(new RoleDTO());
        mockMvc.perform(get("/roles/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreate() throws Exception {
        RoleDTO role = new RoleDTO(null, "Admin", "Description", 1742854217L);
        RoleDTO createdRole = new RoleDTO(1L, "Admin", "Description", 1742854217L);
        when(roleService.create(any(RoleDTO.class))).thenReturn(createdRole);
        mockMvc.perform(post("/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(role)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.roleName").value("Admin"));
        when(roleService.create(any(RoleDTO.class))).thenReturn(new RoleDTO());
        mockMvc.perform(post("/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(role)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdate() throws Exception {
        RoleDTO role = new RoleDTO(1L, "Admin", "Description", 1742854217L);
        RoleDTO updatedRole = new RoleDTO(1L, "Super Admin", "Description Update", 1742854217L);
        when(roleService.update(any(RoleDTO.class))).thenReturn(updatedRole);
        mockMvc.perform(put("/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(role)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.roleName").value("Super Admin"));

        when(roleService.update(any(RoleDTO.class))).thenReturn(new RoleDTO());
        mockMvc.perform(put("/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(role)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteById() throws Exception {
        // BAD REQUEST: ID es null
        RoleDTO nullRole = new RoleDTO(); // ID es null
        when(roleService.deleteById(100L)).thenReturn(nullRole);
        mockMvc.perform(delete("/roles/100"))
                .andExpect(status().isBadRequest());

        // OK: Valid role borrado
        RoleDTO validRole = new RoleDTO(200L, "Admin", "Description", 1742854217L);
        when(roleService.deleteById(200L)).thenReturn(validRole);
        mockMvc.perform(delete("/roles/200"))
                .andExpect(status().isOk());
    }

    @Test
    void testCreate_ReturnsForbidden() throws Exception {
        RoleDTO role = new RoleDTO(null, "Guest", "Basic role", 1742854217L);

        when(roleService.create(any(RoleDTO.class))).thenThrow(
                new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to create roles.")
        );

        mockMvc.perform(post("/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(role)))
                .andExpect(status().isForbidden());
    }

    @Test
    void testUpdate_ReturnsNotFound() throws Exception {
        RoleDTO role = new RoleDTO(999L, "Ghost", "Doesn't exist", 1742854217L);

        when(roleService.update(any(RoleDTO.class))).thenThrow(
                new ResponseStatusException(HttpStatus.NOT_FOUND, "The role to update was not found.")
        );

        mockMvc.perform(put("/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(role)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testFindById_ReturnsNotFoundWithException() throws Exception {
        when(roleService.findById(404L)).thenThrow(
                new ResponseStatusException(HttpStatus.NOT_FOUND, "The role ID: 404 does not exist.")
        );

        mockMvc.perform(get("/roles/404"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreate_ReturnsBadRequestOnFailure() throws Exception {
        RoleDTO invalidRole = new RoleDTO(null, "", "", 0L); // valores inválidos

        when(roleService.create(any(RoleDTO.class))).thenThrow(
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "The role could not be created.")
        );

        mockMvc.perform(post("/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRole)))
                .andExpect(status().isBadRequest());
    }

}
