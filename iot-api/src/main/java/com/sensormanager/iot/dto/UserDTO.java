package com.sensormanager.iot.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserDTO {
    private Long id;
    @NotEmpty(message = "The first name is required")
    private String firstName;
    @NotEmpty(message = "The last name is required")
    private String lastName;
    @NotEmpty(message = "The username is required")
    private String username;
    @NotEmpty(message = "The password ir required")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @NotEmpty(message = "The email is required")
    private String userEmail;
    @NotNull(message = "This field is required")
    private Long userCreatedAt;
    private Long userExpireAt;
    private Boolean userStatus;
    private Long companyId;
    private String companyName;
    private String roleName;
}
