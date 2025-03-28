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
public class RoleDTO {
    private Long id;
    @NotEmpty(message = "The first name is required")
    private String roleName;
    private String roleDescription;
    private Long roleCreatedAt;

}
