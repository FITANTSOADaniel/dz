package com.example.Repository;

import com.example.models.RoleUser;
import com.example.models.Entity.User;
import com.example.repository.User.UserRepositoryJpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import org.junit.jupiter.api.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {

    private static EntityManagerFactory entityManagerFactory;

    private EntityManager entityManager;

    private UserRepositoryJpa userRepositoryJpa;

    @BeforeAll
    static void setupDatabase() {
        entityManagerFactory =
                Persistence.createEntityManagerFactory("testPU");
    }

    @BeforeEach
    void setup() {

        entityManager = entityManagerFactory.createEntityManager();

        userRepositoryJpa = new UserRepositoryJpa(entityManager);
    }

    @AfterEach
    void cleanup() {

        if (entityManager.isOpen()) {
            entityManager.close();
        }
    }

    @AfterAll
    static void closeDatabase() {

        if (entityManagerFactory != null
                && entityManagerFactory.isOpen()) {

            entityManagerFactory.close();
        }
    }

    @Test
    void shouldSaveUser() {
        User user = new User(
            "Rakoto",
            "Daniel",
            "daniel@test.com",
            "0340000000",
            RoleUser.CLIENT,
            new BigDecimal("10000.00")
        );

        userRepositoryJpa.save(user);
        assertNotNull(user.getId());
    }
}