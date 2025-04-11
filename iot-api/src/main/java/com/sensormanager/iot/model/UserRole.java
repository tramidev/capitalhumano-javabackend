package com.sensormanager.iot.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users_roles")
public class UserRole {

    @EmbeddedId
    private UserRoleId id;

    public Integer getUserId() {
        return id != null ? id.getUserId() : null;
    }

    public Integer getRoleId() {
        return id != null ? id.getRoleId() : null;
    }
}
