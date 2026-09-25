package com.example.config;

import java.util.Set;

import org.glassfish.jersey.jackson.JacksonFeature;
import com.example.Controller.UserController.UserController;
import com.example.Controller.TransactionController.TransactionController;

import jakarta.ws.rs.core.Application;

public class RestApplication extends Application{
    @Override
    public Set<Class<?>> getClasses() {
        return Set.of(
            UserController.class,
            TransactionController.class,
            JacksonFeature.class
        );
    }
}
