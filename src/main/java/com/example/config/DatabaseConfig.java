package com.example.config;

import javax.sql.DataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DatabaseConfig {

    private static HikariDataSource dataSource;

    public static synchronized DataSource getDataSource() {
        if (dataSource == null) {
            try {
                // Force le chargement du driver
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Driver PostgreSQL introuvable", e);
            }

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:postgresql://localhost:5432/java_database");
            config.setUsername("postgres");
            config.setPassword("daniel");
            config.setDriverClassName("org.postgresql.Driver"); // ← très important

            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            config.setIdleTimeout(30000);
            config.setConnectionTimeout(20000);

            dataSource = new HikariDataSource(config);
        }
        return dataSource;
    }
}