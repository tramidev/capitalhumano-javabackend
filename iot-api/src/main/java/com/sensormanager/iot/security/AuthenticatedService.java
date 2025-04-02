package com.sensormanager.iot.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticatedService {

    public CustomUserSecurity getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserSecurity) {
            return (CustomUserSecurity) principal;
        }

        return null;
    }

    public boolean isRootUser() {
        CustomUserSecurity user = getAuthenticatedUser();
        return user !=null && user.hasRole("ROOT");
    }

    public Long getUserCompanyId(){
        CustomUserSecurity user = getAuthenticatedUser();
        return (user != null && user.getCompany() != null) ? user.getCompany().getId(): null;
    }

    public boolean isAuthenticated() {
        return getAuthenticatedUser() != null;
    }







}
