package com.example.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;

import com.example.services.JwtService;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class JwtAuthenticationFilter implements ContainerRequestFilter {

    private final JwtService jwtService;

    @Inject
    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public void filter(ContainerRequestContext requestContext)
            throws IOException {

        String path = requestContext
                .getUriInfo()
                .getPath();

        // Routes publiques
        if (path.startsWith("auth/")) {
            return;
        }

        String authorization = requestContext.getHeaderString("Authorization");

        // Pas de header Authorization
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            abort(requestContext, "Token manquant");
            return;
        }
        String token = authorization.substring(7);
        
        try {
            Claims claims = jwtService.validateToken(token);
            String email = claims.getSubject();
            String role = claims.get("role", String.class);
            JwtSecurityContext securityContext = new JwtSecurityContext(email, role);

            requestContext.setSecurityContext(securityContext);

        } catch (JwtException | IllegalArgumentException e) {
            abort(requestContext, "Token invalide");
        }
    }

    private void abort(ContainerRequestContext requestContext, String message) {
        requestContext.abortWith(
            Response.status(Response.Status.UNAUTHORIZED)
                    .entity(message)
                    .build()
        );
    }
}