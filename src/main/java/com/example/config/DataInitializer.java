package com.example.config;

import java.math.BigDecimal;

import com.example.Qualifier.JpaQualifier;
import com.example.models.RoleUser;
import com.example.models.Entity.User;
import com.example.repository.User.UserRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.context.Initialized;
import jakarta.inject.Inject;

@ApplicationScoped
public class DataInitializer {

    private final UserRepository userRepository;

    @Inject
    public DataInitializer(@JpaQualifier UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void init(@Observes @Initialized(ApplicationScoped.class) Object event) {

        System.out.println("=== Initialisation des données ===");

        if (!userRepository.getAll().isEmpty()) {
            System.out.println("La base contient déjà des utilisateurs.");
            return;
        }

        createUsers();

        System.out.println("=== Données initiales créées ===");
    }

    private void createUsers() {

        User user1 = new User(
            "Rakoto",
            "Jean",
            "jean@gmail.com",
            "0340000001",
            RoleUser.CLIENT,
            new BigDecimal("100000.00")
        );

        User user2 = new User(
            "Rabe",
            "Paul",
            "paul@gmail.com",
            "0340000002",
            RoleUser.CLIENT,
            new BigDecimal("50000.00")
        );

        User user3 = new User(
            "Andria",
            "Marie",
            "marie@gmail.com",
            "0340000003",
            RoleUser.CLIENT,
            new BigDecimal("75000.00")
        );

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
    }
}