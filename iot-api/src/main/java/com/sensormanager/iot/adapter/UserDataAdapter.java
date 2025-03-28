package com.sensormanager.iot.adapter;

import com.sensormanager.iot.dto.UserDTO;
import com.sensormanager.iot.model.Company;
import com.sensormanager.iot.model.Role;
import com.sensormanager.iot.model.User;

public class UserDataAdapter {

    public static UserDTO toDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .password(user.getPassword())
                .userEmail(user.getUserEmail())
                .userCreatedAt(user.getUserCreatedAt())
                .userExpireAt(user.getUserExpireAt())
                .userStatus(user.getUserStatus())
                .companyId(user.getCompany() != null ? user.getCompany().getId() : null)
                .companyName(user.getCompany() != null ? user.getCompany().getCompanyName() : null)
                .roleName(user.getRoleName() != null ? user.getRoleName().stream()
                        .map(Role::getRoleName)
                        .reduce((role1, role2) -> role1 + ", " + role2)
                        .orElse(null) : null)
                .build();
    }

    public static User toEntity(UserDTO userDTO) {
        return User.builder()
                .id(userDTO.getId())
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .username(userDTO.getUsername())
                .password(userDTO.getPassword())
                .userEmail(userDTO.getUserEmail())
                .userCreatedAt(userDTO.getUserCreatedAt())
                .userExpireAt(userDTO.getUserExpireAt())
                .userStatus(userDTO.getUserStatus())
                .company(Company.builder().id(userDTO.getCompanyId()).build())
                .companyName(Company.builder().companyName(userDTO.getCompanyName()).build())
                .build();
    }
}