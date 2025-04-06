package com.sensormanager.iot.adapter;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.sensormanager.iot.dto.UserDTO;
import com.sensormanager.iot.model.User;

class UserDataAdapterTest {

    @Test
    @DisplayName("UserDataAdapter Test Entity")
    void testToEntity() {
        UserDTO userDTO = UserDTO.builder()
                .id(1L)
                .firstName("Stephen")
                .lastName("Hawking")
                .username("shawk")
                .password("123passwd")
                .userEmail("shwk@camarones.cl")
                .userCreatedAt(1742854269L)
                .userExpireAt(1748442752L)
                .userStatus(true)
                .companyId(1L)
                .companyName("Minera Camarones")
                .build();

        User user = UserDataAdapter.toEntity(userDTO);

        assertEquals(userDTO.getId(), user.getId());
        assertEquals(userDTO.getFirstName(), user.getFirstName());
        assertEquals(userDTO.getLastName(), user.getLastName());
        assertEquals(userDTO.getUsername(), user.getUsername());
        assertEquals(userDTO.getPassword(), user.getPassword());
        assertEquals(userDTO.getUserEmail(), user.getUserEmail());
        assertEquals(userDTO.getUserCreatedAt(), user.getUserCreatedAt());
        assertEquals(userDTO.getUserExpireAt(), user.getUserExpireAt());
        assertEquals(userDTO.getUserStatus(), user.getUserStatus());
        assertEquals(userDTO.getCompanyId(), user.getCompany().getId());
    }
}