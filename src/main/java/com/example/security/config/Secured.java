package com.example.security.config;

import jakarta.ws.rs.NameBinding;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@NameBinding // 1. Marque cette annotation comme un lien dynamique pour les filtres
@Retention(RetentionPolicy.RUNTIME) // Doit être accessible à l'exécution
@Target({ElementType.TYPE, ElementType.METHOD}) // S'applique sur une classe ou une méthode

public @interface Secured {
    // Optionnel : Vous pouvez ajouter des éléments comme les rôles autorisés
    String[] roles() default {};
}