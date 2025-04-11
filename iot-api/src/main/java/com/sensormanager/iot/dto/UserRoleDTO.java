package com.sensormanager.iot.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserRoleDTO {

    @NotNull(message = "The User ID is required")
    private Integer userId;

    @NotNull(message = "The Role ID is required")
    private Integer roleId;
}
