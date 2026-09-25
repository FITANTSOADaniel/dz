package com.example.Integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.List;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.Controller.UserController.UserController;
import com.example.models.RoleUser;
import com.example.models.Entity.User;
import com.example.repository.User.UserRepositoryJpa;
import com.example.services.UserService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;

public class IntegrationTest extends JerseyTest{
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("testPU");
    private EntityManager em;
    private UserRepositoryJpa userRepository;
    private UserService userService;

    @Override
    protected ResourceConfig configure() {
        // Initialisation de la vraie chaîne de composants
        em = emf.createEntityManager();
        userRepository = new UserRepositoryJpa(em);
        userService = new UserService(userRepository);

        return new ResourceConfig()
            .register(UserController.class)
            .register(JacksonFeature.class)
            .register(new AbstractBinder() {
                @Override
                protected void configure() {
                    // Injection de l'instance réelle (pas un mock)
                    bind(userService).to(UserService.class);
                }
            });
    }

    @BeforeEach
    void seedDatabase() {
        // Nettoyage / Réinitialisation des données avant chaque test
        em.getTransaction().begin();
        em.createQuery("DELETE FROM User").executeUpdate();
        
        User user1 = new User(
            "Rakoto",
            "Daniel",
            "daniel@test.com",
            "0340000000",
            RoleUser.CLIENT,
            new BigDecimal("10000.00")
        );
        User user2 = new User(
            "Rabe",
            "Jean",
            "jean@test.com",
            "Jean",
            RoleUser.CLIENT,
            new BigDecimal("2000.00")
        );
        // User user2 = new User("Rabe", "Jean", "Jean");
        
        em.persist(user1);
        em.persist(user2);
        em.getTransaction().commit();
    }

    @Test
    void shouldGetAllUsersFromDatabase() {
        Response response = target("/users")
                .request()
                .get();

        assertEquals(200, response.getStatus());

        // Désérialisation du corps de la réponse JSON pour valider la persistance réelle
        List<User> users = response.readEntity(new GenericType<List<User>>() {});
        assertEquals(2, users.size());
        assertEquals("Rakoto", users.get(0).getName());
    }

    @AfterEach
    public void tearDown() {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }
}
