package com.sensormanager.iot.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
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
import com.sensormanager.iot.dto.UserDTO;
import com.sensormanager.iot.service.UserService;

public class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testFindAll_ReturnsUsers() throws Exception {
        List<UserDTO> users = Arrays.asList(
                new UserDTO(1L, "Marie", "Curie", "username", "123passwd", "mcurie@anto.cl", 1742854269L, 1742854269L, true, 1L, "Minera", "Admin"),
                new UserDTO(2L, "Marie", "Curie", "username", "123passwd", "mcurie@anto.cl", 1742854269L, 1742854269L, true, 1L, "Minera", "Admin"));
        when(userService.findAll()).thenReturn(users);
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(users.size()));
        verify(userService, times(1)).findAll();
    }

    @Test
    public void testFindAll_ReturnsNoContent() throws Exception {
        when(userService.findAll()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/users"))
                .andExpect(status().isNoContent());
        verify(userService, times(1)).findAll();
    }

    @Test
    public void testFindById_ReturnsUser() throws Exception {
        UserDTO user = new UserDTO(1L, "Marie", "Curie", "username", "123passwd", "mcurie@anto.cl", 1742854269L, 1742854269L, true, 1L, "Minera", "Admin");
        when(userService.findById(1L)).thenReturn(user);
        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.firstName").value(user.getFirstName()));
        verify(userService, times(1)).findById(1L);
    }

    @Test
    public void testFindById_ReturnsNotFound() throws Exception {
        UserDTO user = new UserDTO(null, null, null, null, null, null, null, null, null, null, null, null);
        when(userService.findById(1L)).thenReturn(user);
        mockMvc.perform(get("/users/1"))
                .andExpect(status().isNotFound());
        verify(userService, times(1)).findById(1L);
    }

    @Test
    public void testCreate_ReturnsCreatedUser() throws Exception {
        UserDTO user = new UserDTO(null, "Marie", null, null, null, null, null, null, null, null, null, null);
        UserDTO createdUser = new UserDTO(2L, "Marie", null, null, null, null, null, null, null, null, null, null);
        when(userService.create(any(UserDTO.class))).thenReturn(createdUser);
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdUser.getId()))
                .andExpect(jsonPath("$.firstName").value(createdUser.getFirstName()));
        verify(userService, times(1)).create(any(UserDTO.class));
    }

    @Test
    public void testCreate_ReturnsBadRequest() throws Exception {
        UserDTO user = new UserDTO(null, "Marie", null, null, null, null, null, null, null, null, null, null);
        UserDTO createdUser = new UserDTO(null, null, null, null, null, null, null, null, null, null, null, null);
        when(userService.create(any(UserDTO.class))).thenReturn(createdUser);
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
        verify(userService, times(1)).create(any(UserDTO.class));
    }

    @Test
    public void testDeleteById_ReturnsDeletedUser() throws Exception {
        UserDTO deletedUser = new UserDTO(2L, "Marie", null, null, null, null, null, null, null, null, null, null);
        when(userService.deleteById(1L)).thenReturn(deletedUser);
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(deletedUser.getId()))
                .andExpect(jsonPath("$.firstName").value(deletedUser.getFirstName()));
        verify(userService, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteById_ReturnsBadRequest() throws Exception {
        UserDTO deletedUser = new UserDTO(200L, "Marie", null, null, null, null, null, null, null, null, null, null);
        when(userService.deleteById(200L)).thenReturn(deletedUser);
        mockMvc.perform(delete("/users/200"))
                .andExpect(status().isBadRequest());
        verify(userService, times(1)).deleteById(200L);
    }

    @Test
    public void testUpdate_ReturnsUpdatedUser() throws Exception {
        UserDTO user = new UserDTO(2L, "Marie", null, null, null, null, null, null, null, null, null, null);
        UserDTO updatedUser = new UserDTO(2L, "Marie Updated", null, null, null, null, null, null, null, null, null, null);
        when(userService.update(any(UserDTO.class))).thenReturn(updatedUser);
        mockMvc.perform(put("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedUser.getId().intValue()))
                .andExpect(jsonPath("$.firstName").value(updatedUser.getFirstName()));
        verify(userService, times(1)).update(any(UserDTO.class));
    }

    @Test
    public void testUpdate_ReturnsNotFound() throws Exception {
        UserDTO user = new UserDTO(2L, "Marie", null, null, null, null, null, null, null, null, null, null);
        UserDTO updatedUser = new UserDTO(null, null, null, null, null, null, null, null, null, null, null, null);
        when(userService.update(any(UserDTO.class))).thenReturn(updatedUser);
        mockMvc.perform(put("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isNotFound());
        verify(userService, times(1)).update(any(UserDTO.class));
    }
}
