package com.example.Integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
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

public class IntegrationTest extends JerseyTest {

    private static final String CONTAINER_NAME = "postgres-test-db";
    private static EntityManagerFactory emf;
    private EntityManager em;
    private UserRepositoryJpa userRepository;
    private UserService userService;

    // Méthode utilitaire pour exécuter n'importe quelle commande shell depuis Java
    private static void executeCommand(String... command) throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        // Lecture des logs en cas de besoin
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("[DOCKER] " + line);
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0 && !command[1].equals("rm")) { // Ignorer si le conteneur n'existait pas lors du rm
            System.err.println("La commande a échoué avec le code : " + exitCode);
        }
    }

    @BeforeAll
    static void initContainerAndDb() throws Exception {
        // 1. Nettoyer un ancien conteneur s'il tourne déjà
        executeCommand("docker", "rm", "-f", CONTAINER_NAME);

        // 2. Démarrer le conteneur PostgreSQL
        System.out.println("Démarrage du conteneur Docker PostgreSQL...");
        executeCommand(
            "docker", "run", "-d",
            "--name", CONTAINER_NAME,
            "-e", "POSTGRES_DB=testdb",
            "-e", "POSTGRES_USER=test",
            "-e", "POSTGRES_PASSWORD=test",
            "-p", "5433:5432",
            "postgres:16-alpine"
        );

        // 3. Attendre que PostgreSQL soit prêt à accepter des connexions
        System.out.println("Attente du démarrage complet de PostgreSQL...");
        int attempts = 0;
        boolean ready = false;
        while (attempts < 15 && !ready) {
            try {
                ProcessBuilder checkBuilder = new ProcessBuilder(
                    "docker", "exec", CONTAINER_NAME, "pg_isready", "-U", "test"
                );
                Process checkProcess = checkBuilder.start();
                if (checkProcess.waitFor() == 0) {
                    ready = true;
                } else {
                    Thread.sleep(1000);
                    attempts++;
                }
            } catch (Exception e) {
                Thread.sleep(1000);
                attempts++;
            }
        }

        if (!ready) {
            throw new RuntimeException("PostgreSQL n'a pas pu démarrer dans le temps imparti !");
        }

        // 4. Initialiser Hibernate avec l'URL pointant vers le conteneur sur le port 5433
        Map<String, Object> properties = new HashMap<>();
        properties.put("jakarta.persistence.jdbc.url", "jdbc:postgresql://localhost:5433/testdb");
        properties.put("jakarta.persistence.jdbc.user", "test");
        properties.put("jakarta.persistence.jdbc.password", "test");

        emf = Persistence.createEntityManagerFactory("testContainers", properties);
    }

    @Override
    protected ResourceConfig configure() {
        if (emf == null) {
            try {
                initContainerAndDb();
            } catch (Exception e) {
                throw new RuntimeException("Erreur lors de l'initialisation du conteneur", e);
            }
        }
        em = emf.createEntityManager();
        userRepository = new UserRepositoryJpa(em);
        userService = new UserService(userRepository);

        return new ResourceConfig()
            .register(UserController.class)
            .register(JacksonFeature.class)
            .register(new AbstractBinder() {
                @Override
                protected void configure() {
                    bind(userService).to(UserService.class);
                }
            });
    }

    @BeforeEach
    void seedDatabase() {
        em.getTransaction().begin();
        em.createQuery("DELETE FROM User").executeUpdate();

        User user1 = new User(
            "Rakoto", "Daniel", "daniel@test.com", "0340000000",
            RoleUser.CLIENT, new BigDecimal("10000.00")
        );
        User user2 = new User(
            "Rabe", "Jean", "jean@test.com", "0340000001",
            RoleUser.CLIENT, new BigDecimal("2000.00")
        );

        em.persist(user1);
        em.persist(user2);
        em.getTransaction().commit();
    }

    @Test
    void shouldGetAllUsersFromDatabase() {
        Response response = target("/users").request().get();

        assertEquals(200, response.getStatus());

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

    @AfterAll
    static void closeContainerAndDb() throws Exception {
        if (emf != null) {
            emf.close();
        }

        // Arrêter et supprimer le conteneur à la fin de tous les tests
        System.out.println("Arrêt et suppression du conteneur Docker...");
        executeCommand("docker", "rm", "-f", CONTAINER_NAME);
    }
}