package com.example.security.filter;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;

import java.io.IOException;

@Priority(Priorities.AUTHORIZATION)
public class RolesAllowedFilter implements ContainerRequestFilter {

    private final String[] allowedRoles;
    // Recevoir les Roles venus de AuthorizationDynamicFeature
    public RolesAllowedFilter(String[] allowedRoles) {
        this.allowedRoles = allowedRoles;
    }
    //Recupérer le securityContext et vérifier si le role de l'utilisateur cette requete peut executer la requete
    // si oui , va vers le controlleur sinon Accès refusé
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        for (String role : allowedRoles) {
            if (requestContext.getSecurityContext().isUserInRole(role)) {
                return;
            }
        }
        requestContext.abortWith(Response.status(Response.Status.FORBIDDEN)
                        .entity("Accès refusé")
                        .build()
        );
    }
}