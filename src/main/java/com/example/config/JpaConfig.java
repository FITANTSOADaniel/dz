package com.example.config;

import java.util.HashMap;
import java.util.Map;

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
        Map<String, Object> properties = new HashMap<>();
        properties.put("jakarta.persistence.jdbc.url",System.getenv("DB_URL"));
        properties.put("jakarta.persistence.jdbc.user",System.getenv("DB_USER"));
        properties.put("jakarta.persistence.jdbc.password",System.getenv("DB_PASSWORD"));
        emf = Persistence.createEntityManagerFactory("myPersistenceUnit", properties);
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