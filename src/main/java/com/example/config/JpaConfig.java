package com.example.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

@ApplicationScoped
public class JpaConfig {

    private final EntityManagerFactory emf;

    public JpaConfig() {
        emf = Persistence.createEntityManagerFactory("myPersistenceUnit");
    }

    @Produces
    @ApplicationScoped
    public EntityManager createEntityManager() {
        return emf.createEntityManager();
    }

    public void closeEntityManager(
            @Disposes EntityManager entityManager
    ) {
        if (entityManager.isOpen()) {
            entityManager.close();
        }
    }
}