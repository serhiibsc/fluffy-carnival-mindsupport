package com.diploma.mindsupport.model;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {
    VOLUNTEER, ADMIN, PATIENT;

    @Override
    public String getAuthority() {
        return name();
    }
}
