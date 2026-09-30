package com.example.security;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.container.DynamicFeature;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.FeatureContext;

public class AuthorizationDynamicFeature implements DynamicFeature {

    // Cette classe va récupérer les valeurs dans le @RolesAllowed des controlleurs
    // @RolesAllowed("USER")
    // @RolesAllowed("ADMIN")
    // Les envoyés vers le RolesAllowedFilter
    @Override
    public void configure(ResourceInfo resourceInfo, FeatureContext context) {
        RolesAllowed rolesAllowed =resourceInfo.getResourceMethod().getAnnotation(RolesAllowed.class);

        if (rolesAllowed == null) {
            rolesAllowed = resourceInfo.getResourceClass().getAnnotation(RolesAllowed.class);
        }

        if (rolesAllowed != null) {
            context.register(new RolesAllowedFilter(rolesAllowed.value()));
        }
    }
}