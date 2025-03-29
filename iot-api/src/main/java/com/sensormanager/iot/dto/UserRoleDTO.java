package com.sensormanager.iot.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserRoleDTO {
    @NotEmpty(message = "The User ID is required")
    private Integer userId;
    @NotEmpty(message = "The Role ID is required")
    private Integer roleId;
}
