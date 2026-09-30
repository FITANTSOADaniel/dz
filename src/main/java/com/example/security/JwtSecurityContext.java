package com.example.security;

import jakarta.ws.rs.core.SecurityContext;
import java.security.Principal;

public class JwtSecurityContext implements SecurityContext {

    private final String email;
    private final String role;

    public JwtSecurityContext(String email, String role) {
        this.email = email;
        this.role = role;
    }

    @Override
    public Principal getUserPrincipal() {
        return () -> email;
    }

    @Override
    public boolean isUserInRole(String role) {
        return this.role.equals(role);
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public String getAuthenticationScheme() {
        return "Bearer";
    }
}