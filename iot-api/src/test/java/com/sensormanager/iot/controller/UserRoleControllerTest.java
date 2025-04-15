package com.sensormanager.iot.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sensormanager.iot.dto.UserRoleDTO;
import com.sensormanager.iot.service.UserRoleService;
import org.springframework.web.server.ResponseStatusException;

class UserRoleControllerTest {

    @Mock
    private UserRoleService userRoleService;

    @InjectMocks
    private UserRoleController userRoleController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userRoleController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testUpdateUserRole() throws Exception {
        UserRoleDTO userRoleDTO = new UserRoleDTO();
        userRoleDTO.setUserId(1);
        userRoleDTO.setRoleId(1);
        UserRoleDTO updatedUserRoleDTO = new UserRoleDTO();
        updatedUserRoleDTO.setUserId(1);
        updatedUserRoleDTO.setRoleId(2);
        when(userRoleService.update(any(UserRoleDTO.class))).thenReturn(updatedUserRoleDTO);
        mockMvc.perform(put("/user-role")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRoleDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.roleId").value(2));
        verify(userRoleService, times(1)).update(any(UserRoleDTO.class));
    }

    @Test
    void testUpdateUserRole_Returns403Forbidden() throws Exception {
        UserRoleDTO userRoleDTO = new UserRoleDTO();
        userRoleDTO.setUserId(1);
        userRoleDTO.setRoleId(1);

        when(userRoleService.update(any(UserRoleDTO.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied."));

        mockMvc.perform(put("/user-role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRoleDTO)))
                .andExpect(status().isForbidden());

        verify(userRoleService, times(1)).update(any(UserRoleDTO.class));
    }

    @Test
    void testUpdateUserRole_Returns404NotFound() throws Exception {
        UserRoleDTO userRoleDTO = new UserRoleDTO();
        userRoleDTO.setUserId(99); // UserId inexistente
        userRoleDTO.setRoleId(1);

        when(userRoleService.update(any(UserRoleDTO.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "User role not found."));

        mockMvc.perform(put("/user-role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRoleDTO)))
                .andExpect(status().isNotFound());

        verify(userRoleService, times(1)).update(any(UserRoleDTO.class));
    }

    @Test
    void testCreateUserRole_Returns400BadRequest_DuplicatedRole() throws Exception {
        UserRoleDTO userRoleDTO = new UserRoleDTO();
        userRoleDTO.setUserId(1);
        userRoleDTO.setRoleId(1);

        when(userRoleService.create(any(UserRoleDTO.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "This user already has an assigned role."));

        mockMvc.perform(
                        org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/user-role")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(userRoleDTO)))
                .andExpect(status().isBadRequest());

        verify(userRoleService, times(1)).create(any(UserRoleDTO.class));
    }

}