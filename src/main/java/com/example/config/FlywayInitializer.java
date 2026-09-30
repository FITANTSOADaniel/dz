package com.example.config;

import org.flywaydb.core.Flyway;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;

@ApplicationScoped
public class FlywayInitializer {
    public void init(
        @Observes @Initialized(ApplicationScoped.class) Object event
    ) {

        Flyway flyway = Flyway.configure()
                .dataSource(
                    System.getenv("DB_URL"),
                    System.getenv("DB_USER"),
                    System.getenv("DB_PASSWORD")
                )
                .load();

        flyway.migrate();
    }
}
