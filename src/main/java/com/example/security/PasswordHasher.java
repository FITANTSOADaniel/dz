package com.example.security;

import jakarta.enterprise.context.ApplicationScoped;
import org.mindrot.jbcrypt.BCrypt;

@ApplicationScoped
public class PasswordHasher {

    private static final int SALT_ROUNDS = 12;

    public String hash(String password) {
        return BCrypt.hashpw(
            password,
            BCrypt.gensalt(SALT_ROUNDS)
        );
    }

    public boolean verify(String password, String hash) {
        return BCrypt.checkpw(password, hash);
    }
}