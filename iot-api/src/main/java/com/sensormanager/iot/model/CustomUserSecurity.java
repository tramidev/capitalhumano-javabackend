package com.sensormanager.iot.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;

import java.util.Collection;

@Getter
public class CustomUserSecurity extends User {

    private Company company;

    // Constructor
    public CustomUserSecurity(String username, String password, boolean enabled, Collection<? extends GrantedAuthority> authorities, Company company) {
        super(username, password, enabled, true, true, true, authorities);
        this.company = company;
    }
    
    public boolean hasRole(String role) {
        for (GrantedAuthority authority : this.getAuthorities()) {
            if (authority.getAuthority().equals(role)) {
                return true;
            }
        }
        return false;
    }

}

