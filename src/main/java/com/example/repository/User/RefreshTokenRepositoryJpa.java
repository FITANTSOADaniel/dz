package com.example.repository.User;

import com.example.models.Entity.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import lombok.NoArgsConstructor;

@NoArgsConstructor 
@ApplicationScoped
public class RefreshTokenRepositoryJpa {

    private EntityManager entityManager;

    @Inject 
    public RefreshTokenRepositoryJpa(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public RefreshToken save(RefreshToken refreshToken) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {
            entityManager.persist(refreshToken);
            entityManager.flush();

            transaction.commit();

        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
        return refreshToken;
    }

    public RefreshToken findByTokenHash(String tokenHash) {
        try {
            return entityManager
                    .createQuery("SELECT r FROM RefreshToken r JOIN FETCH r.user WHERE r.tokenHash = :tokenHash", RefreshToken.class)
                    .setParameter("tokenHash", tokenHash)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    public void delete(RefreshToken refreshToken) {
        entityManager.remove(refreshToken);
    }
}